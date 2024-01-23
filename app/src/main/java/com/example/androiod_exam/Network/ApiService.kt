package com.example.androiod_exam.Network

import com.example.androiod_exam.DataClass.Product
import com.google.android.engage.shopping.datamodel.ShoppingCart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>

  //  @GET("carts")
  //  suspend fun getAllCarts(): List<ShoppingCart>
}