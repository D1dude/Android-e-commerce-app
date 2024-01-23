package com.example.androiod_exam.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiod_exam.DataClass.entity.OrderEntity
import com.example.androiod_exam.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException


class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _orderItems = MutableLiveData<List<OrderEntity>>()
    val orderItems: LiveData<List<OrderEntity>> get() = _orderItems

    private val _filteredOrderItems = MutableLiveData<List<OrderEntity>>()
    val filteredOrderItems: LiveData<List<OrderEntity>> get() = _filteredOrderItems



    init {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _orderItems.postValue(orderRepository.getOrders())
            } catch (e: CancellationException) {
                // Ignorer avbrutte korutiner
            } catch (e: Exception) {
                // Logg feil her for å få mer informasjon
                Log.e("ShoppingCartViewModel", "Error in init block", e)
            }
        }
    }
    fun getAllOrders() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _orderItems.postValue(orderRepository.getOrders())
            } catch (e: CancellationException) {
                // Ignorer avbrutte korutiner
            } catch (e: Exception) {
                // Logg feil her for å få mer informasjon
                Log.e("OrderViewModel", "Error getting all orders", e)
            }
        }
    }

   // suspend fun getOrdersByCartId(cartId: String): List<OrderEntity> { return orderRepository.getOrdersByCartId(cartId) }
    suspend fun getOrdersByCartId(cartId: String) {
        val orders = orderRepository.getOrdersByCartId(cartId)
        _filteredOrderItems.postValue(orders)
    }



    suspend fun setFilteredOrderItemsByCartId(cartId: String) {
        try {
            val orders = orderRepository.getOrdersByCartId(cartId)
            _filteredOrderItems.postValue(orders)
        } catch (e: CancellationException) {
            // Ignorer avbrutte korutiner
        } catch (e: Exception) {
            // Logg feil her for å få mer informasjon
            Log.e("OrderViewModel", "Error setting filtered order items", e)
        }
    }


    // Andre egenskaper og funksjoner i OrderViewModel
}