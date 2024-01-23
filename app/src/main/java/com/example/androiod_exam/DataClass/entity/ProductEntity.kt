package com.example.androiod_exam.DataClass.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val price: String,
    val category: String,
    val image: String,
    val description: String
)
