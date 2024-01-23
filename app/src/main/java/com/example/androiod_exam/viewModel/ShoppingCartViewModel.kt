package com.example.androiod_exam.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiod_exam.DataClass.entity.CartItemEntity
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.repository.ShoppingCartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException


class ShoppingCartViewModel(private val shoppingCartRepository: ShoppingCartRepository, private val viewModelScope: CoroutineScope) : ViewModel() {
    val cartItem: LiveData<CartItemEntity> get() = _cartItem


    private val _products = MutableLiveData<List<ProductEntity>>(emptyList())
    val products: LiveData<List<ProductEntity>> get() = _products

    private val _cartItemAdded = MutableLiveData<Boolean>()
    val cartItemAdded: LiveData<Boolean> get() = _cartItemAdded

    private val _cartItems = MutableLiveData<List<CartItemEntity>>()
   val cartItems: LiveData<List<CartItemEntity>> get() = _cartItems

    private val _cartItem = MutableLiveData<CartItemEntity>()


    fun updateQuantity(newQuantity: Int) { _cartItem.value = _cartItem.value?.copy(quantity = newQuantity) }



    init {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _cartItems.postValue(shoppingCartRepository.getAllCartItems())
            } catch (e: CancellationException) {
                // Ignorer avbrutte korutiner
            } catch (e: Exception) {
                // Logg feil her for å få mer informasjon
                Log.e("ShoppingCartViewModel", "Error in init block", e)
            }
        }
    }



    suspend fun getAllCartItems() { _cartItems.value = shoppingCartRepository.getAllCartItems() }


    fun addToCart(product: ProductEntity?) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                // Sjekk om product er null før du prøver å få tilgang til id
                if (product != null) {
                    val productId = product.id ?: 0

                    // Hent den høyeste cartId fra ordre
                    val maxCartId = shoppingCartRepository.getMaxOrderCartId()?.toIntOrNull() ?: 0

                    // Øk mengden hvis produktet allerede finnes i handlekurven
                    val existingCartItem = _cartItems.value?.find { it.productId == productId }

                    if (existingCartItem != null) {
                        existingCartItem.quantity += 1
                        shoppingCartRepository.updateCartItem(existingCartItem)
                    } else {
                        // Legg til nytt element i handlekurven
                        val cartItem = CartItemEntity(
                            cartId = (maxCartId + 1),
                            productId = productId,
                            quantity = 1,
                            price = product.price,
                            title = product.title,
                            image = product.image
                        )
                        shoppingCartRepository.insertCartItem(cartItem)
                    }

                    // Oppdater _cartItems etter endringer
                    _cartItems.value = shoppingCartRepository.getAllCartItems()
                } else {
                    // Håndter situasjonen der product er null (valgfritt)
                    // Dette kan være et sted å gi tilbakemelding eller håndtere feil
                }
            } catch (e: Exception) {
                // Håndter feil her, logg eller gi tilbakemelding til brukeren
                Log.e("ShoppingCartViewModel", "Error in addToCart", e)
            }
        }
    }




    fun removeCartItem(cartItem: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoppingCartRepository.removeCartItem(cartItem)
                withContext(Dispatchers.Main) {
                    _cartItems.value = shoppingCartRepository.getAllCartItems()
                }
            } catch (e: Exception) {
                // Håndter feil her, logg eller gi tilbakemelding til brukeren
                Log.e("ShoppingCartViewModel", "Error in removeCartItem", e)
            }
        }
    }


    fun increaseCartItemQuantity(cartItem: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            // Øk mengden i lokale variabler først
            val updatedQuantity = cartItem.quantity + 1

            // Oppdater databasen med den oppdaterte varen
            shoppingCartRepository.updateCartItemQuantity(cartItem.id, updatedQuantity)

            // Dersom du ønsker å oppdatere LiveData også, kan du gjøre det som følger:
            withContext(Dispatchers.Main) {
                // Hent den gjeldende verdien av _cartItems
                val currentCartItems = _cartItems.value.orEmpty().toMutableList()

                // Finn og oppdater det eksisterende elementet
                currentCartItems.find { it.id == cartItem.id }?.quantity = updatedQuantity

                // Sett den oppdaterte listen til _cartItems LiveData
                _cartItems.value = currentCartItems
            }
        }
    }

    fun decreaseCartItemQuantity(cartItem: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (cartItem.quantity > 1) {
                // Reduser mengden i lokale variabler først
                val updatedQuantity = cartItem.quantity - 1

                // Oppdater databasen med den oppdaterte varen
                shoppingCartRepository.updateCartItemQuantity(cartItem.id, updatedQuantity)

                // Oppdater _cartItems LiveData med den nye listen etter oppdatering
                withContext(Dispatchers.Main) {
                    // Hent den gjeldende verdien av _cartItems
                    val currentCartItems = _cartItems.value.orEmpty().toMutableList()

                    // Finn og oppdater det eksisterende elementet
                    currentCartItems.find { it.id == cartItem.id }?.quantity = updatedQuantity

                    // Sett den oppdaterte listen til _cartItems LiveData
                    _cartItems.value = currentCartItems
                }
            }
        }
    }




    init { _cartItems.observeForever { calculateTotalPrice(it)} }

    private val _totalPrice = MutableLiveData<Double>().apply { value = 0.0 }
    val totalPrice: LiveData<Double> = _totalPrice


    fun calculateTotalPrice(cartItems: List<CartItemEntity>) {
        if (true) {
            var totalPrice = 0.0
            for (cartItem in cartItems) {
                totalPrice += (cartItem.quantity * cartItem.price.toDouble())
            }
            _totalPrice.value = totalPrice
        } else {
            // Logg eller håndter tilfelle der _totalPrice er null
        }
    }


    // Genererer dsgens dato som en streng
    fun getCurrentDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun createOrder(cartItems: List<CartItemEntity>): Unit? {
        return runBlocking {
            try {
                val currentDate = getCurrentDate()
                shoppingCartRepository.createOrder(cartItems, currentDate)

                shoppingCartRepository.deleteAllCartItems()

                _cartItems.value = emptyList() // Oppdater LiveData med tom handlekurv
                null
            } catch (e: Exception) {
                // Håndter feil her, logg eller gi tilbakemelding til brukeren
                Log.e("ShoppingCartViewModel", "Error processing order", e)
                throw e
            }
        }
    }
}









