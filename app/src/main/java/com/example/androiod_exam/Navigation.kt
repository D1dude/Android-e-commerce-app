package com.example.androiod_exam

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androiod_exam.DataClass.AppContainer
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.Network.RetrofitBuilder
import com.example.androiod_exam.screens.OrderScreen
import com.example.androiod_exam.screens.ProductList
import com.example.androiod_exam.screens.ProductScreen
import com.example.androiod_exam.screens.Screen
import com.example.androiod_exam.screens.ShoppingCartScreen
import com.example.androiod_exam.viewModel.OrderViewModel
import com.example.androiod_exam.viewModel.ProductViewModel
import com.example.androiod_exam.viewModel.ShoppingCartViewModel
//import com.example.androiod_exam.viewModel.ShoppingCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable

fun Navigation(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    appContainer: AppContainer,
    orderViewModel: OrderViewModel
) {
    var productList by remember { mutableStateOf<List<ProductEntity>>(emptyList()) }

    DisposableEffect(Unit) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val products = RetrofitBuilder.apiService.getProducts()
                productList = products.map { product ->
                    ProductEntity(
                        id = product.id,
                        title = product.title,
                        price = product.price,
                        category = product.category,
                        image = product.image,
                        description = product.description
                    )
                }
                appContainer.productViewModel.getProducts() // Oppdaterer ViewModel med de nye produktene
            } catch (e: Exception) {
                print("fail ${e.message}")
            }
        }
        onDispose { scope.cancel() }
    }

    NavHost(navController = navController, startDestination = Screen.ProductList.route) {
        composable(Screen.ProductList.route) {
           // Surface { ProductList(productList, navController = navController) }
            Surface { ProductList(productViewModel = productViewModel, navController = navController) }
            }



        /*composable("${Screen.ProductScreen.route}/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            // Hent produktinformasjon basert på productId og vis den i ProductScreen
            val product = productList.find { it.id.toString() == productId }
            if (product != null) {
                ProductScreen(product,
                    navController = navController,
                    productViewModel = productViewModel,
                    shoppingCartViewModel= shoppingCartViewModel)
            } else {
                // Håndter feil her, for eksempel produktet ikke funnet
            }
        }

         */



       composable("${Screen.ProductScreen.route}/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")

            // Observerer LiveData og konverterer det til en State
            val products by productViewModel.products

            // Hent produktinformasjon basert på productId fra databasen
            val product = products.find { it.id.toString() == productId }

            if (product != null) {
                ProductScreen(
                    product = product,
                    navController = navController,
                    productViewModel = productViewModel,
                    shoppingCartViewModel = shoppingCartViewModel
                )
            } else {
                // Håndter feil her, for eksempel produktet ikke funnet
            }
        }




        composable(Screen.ShoppingCart.route) {
            // Vis ShoppingCartScreen her og pass nødvendige parametere
            ShoppingCartScreen(
                cartItems = shoppingCartViewModel.cartItems.value ?: emptyList(),
                navController = navController,
                shoppingCartViewModel = shoppingCartViewModel,

            )
        }
        // Legg til denne composable for OrderScreen
        composable(Screen.OrderScreen.route) {
            OrderScreen(
                orderViewModel = orderViewModel,
                navController = navController
            )
        }
    }
}