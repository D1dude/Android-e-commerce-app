package com.example.androiod_exam.repository

import android.util.Log
import com.example.androiod_exam.DataClass.dao.CartItemDao
import com.example.androiod_exam.DataClass.dao.OrderDao
import com.example.androiod_exam.DataClass.dao.ProductDao
import com.example.androiod_exam.DataClass.entity.CartItemEntity
import com.example.androiod_exam.DataClass.entity.OrderEntity
import com.example.androiod_exam.Network.ApiService
import kotlinx.coroutines.CoroutineScope
class ShoppingCartRepository(
    private val apiService: ApiService,
    private val cartItemDao: CartItemDao,
    private val productDao: ProductDao,
    private val orderDao: OrderDao,
    private val viewModelScope: CoroutineScope, // La til denne linjen
) {

    // Funksjon for å hente alle elementer i handlekurven fra databasen
    suspend fun getAllCartItems(): List<CartItemEntity> {
        return cartItemDao.getAllCartItems()
    }
    // Funksjon for å oppdatere et produkt handlekurv databasen
    suspend fun updateCartItem(cartItem: CartItemEntity) {
        cartItemDao.updateCartItem(cartItem)
    }
    // Funksjon for å legge til produkt i handlekurv databasen
    suspend fun insertCartItem(cartItem: CartItemEntity) {
        cartItemDao.insertCartItem(cartItem)
    }
    // Funksjon for å oppdatere antallet av et produkt i handlekurven
    suspend fun updateCartItemQuantity(cartItemId: Int, newQuantity: Int) {
        val existingCartItem: CartItemEntity? = cartItemDao.getCartItemById(cartItemId)

        existingCartItem?.let {
            it.quantity = newQuantity
            updateCartItem(it)
        }
    }
    // Funksjon for å fjerne et produkt fra handlekurven
    suspend fun removeCartItem(cartItem: CartItemEntity) {
        cartItemDao.removeCartItem(cartItem)
    }
    // Funksjon for å finne den høyeset cartId fra ordredatabasen
    suspend fun getMaxOrderCartId(): String? {
        return orderDao.getMaxOrderCartId()
    }
    // Funksjon for å slette alt i handlekurv databasen
    suspend fun deleteAllCartItems() {
        cartItemDao.deleteAllCartItems()
    }


    // Funksjon for å opprette en ny bestilling basert på elementene i handlekurven
    suspend fun createOrder(cartItems: List<CartItemEntity>, currentDate: String): Unit? {
        return try {
            val orders = cartItems.map { cartItem ->
                OrderEntity(
                    cartId = cartItem.cartId,
                    productId = cartItem.productId,
                    quantity = cartItem.quantity,
                    title = cartItem.title,
                    price = cartItem.price,
                    image = cartItem.image,
                    date = currentDate
                )
            }
            // legger inn objektene fra handlekurven til ordredatabasen
            orderDao.insertOrders(orders)
            null
        } catch (e: Exception) {
            //
            Log.e("ShoppingCartRepository", "Feil i createOrder", e)
            throw e
        }
    }
}