package com.example.androiod_exam.DataClass.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val cartId: Int,
    val productId: Int,
    var quantity: Int,
    val price: String,
    val title: String,
    val image: String,
)