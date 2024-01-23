package com.example.androiod_exam.DataClass.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0,
    val cartId: Int,
    val productId: Int,
    var quantity: Int,
    val title: String,
    val price: String,
    val image: String,
    val date: String
)

