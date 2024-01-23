package com.example.androiod_exam.screens

sealed class Screen(val route:String){
    object ProductList : Screen("productList")
    object ProductScreen : Screen("productScreen")
    object ShoppingCart : Screen("shopingCartScreen")
    object OrderScreen : Screen("orderScreen")
}
