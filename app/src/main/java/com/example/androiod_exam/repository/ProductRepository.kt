package com.example.androiod_exam.repository

import com.example.androiod_exam.DataClass.dao.ProductDao
import com.example.androiod_exam.DataClass.entity.ProductEntity
import com.example.androiod_exam.Network.ApiService
/*
class ProductRepository(private val apiService: ApiService) {
    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }
}
*/



class ProductRepository(private val apiService: ApiService, private val productDao: ProductDao) {


    suspend fun getAllProducts(): List<ProductEntity> {
        return productDao.getAllProducts()
    }



    suspend fun getProducts(): List<ProductEntity> {
        // Hent produkter fra API-et
        val products = apiService.getProducts()

        // Konverter Product til ProductEntity og lagre i databasen
        val productEntities = products.map { product ->
            ProductEntity(
                id = product.id,
                title = product.title,
                price = product.price,
                category = product.category,
                image = product.image,
                description = product.description
            )
        }

        // Lagre produktene i databasen
        productDao.insertProducts(productEntities)

        // Returner produktene
        return productEntities
    }
}