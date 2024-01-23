package com.example.androiod_exam.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.repository.ProductRepository
import kotlinx.coroutines.launch


class ProductViewModel(
    private val repository: ProductRepository,
    private val shoppingCartViewModel: ShoppingCartViewModel
) : ViewModel() {

    private val _products = mutableStateOf<List<ProductEntity>>(emptyList())
    val products: State<List<ProductEntity>> = _products

    // Coroutine for å hente produkter
    fun getProducts() {
        viewModelScope.launch {
            _products.value = runCatching { repository.getProducts() }.getOrElse { emptyList() }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _products.value = runCatching { repository.getAllProducts() }.getOrElse { emptyList() }
        }
    }

    fun addToCart(product: ProductEntity?) {
        viewModelScope.launch {
            shoppingCartViewModel.addToCart(product)
        }
    }
}