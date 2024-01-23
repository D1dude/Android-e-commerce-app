package com.example.androiod_exam.DataClass

import android.content.Context
import com.example.androiod_exam.Network.ApiService
import com.example.androiod_exam.Network.createApiService
import com.example.androiod_exam.repository.OrderRepository
import com.example.androiod_exam.repository.ProductRepository
import com.example.androiod_exam.repository.ShoppingCartRepository
import com.example.androiod_exam.viewModel.OrderViewModel
import com.example.androiod_exam.viewModel.ProductViewModel
import com.example.androiod_exam.viewModel.ShoppingCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers



class AppContainer(context: Context) {
    // Database-instans opprettet ved hjelp av Singleton-metoden i AppDatabase
    private val appDatabase: AppDatabase = getDatabase(context)
    // API-tjeneste for nettverkskall
    private val apiService: ApiService = createApiService()

    // CoroutineScope brukt for å håndtere asynkrone operasjoner i ViewModels
    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    // Repositories som gir tilgang til data
    private val productRepository: ProductRepository = ProductRepository(apiService, appDatabase.productDao())
    private val orderRepository: OrderRepository = OrderRepository(appDatabase.orderDao())
    private val shoppingCartRepository: ShoppingCartRepository = ShoppingCartRepository(
        apiService,
        appDatabase.cartItemDao(),
        appDatabase.productDao(),
        appDatabase.orderDao(),
        viewModelScope // Bruk viewModelScope her
    )

    // ViewModels som gir data for UI-komponenter
    val orderViewModel: OrderViewModel = OrderViewModel(orderRepository)
    val shoppingCartViewModel: ShoppingCartViewModel = ShoppingCartViewModel(shoppingCartRepository, viewModelScope)
    val productViewModel: ProductViewModel = ProductViewModel(productRepository, shoppingCartViewModel)

    private fun getDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }
}