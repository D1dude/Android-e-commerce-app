package com.example.androiod_exam.DataClass


import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase
import com.example.androiod_exam.DataClass.dao.CartItemDao
import com.example.androiod_exam.DataClass.dao.OrderDao
import com.example.androiod_exam.DataClass.dao.ProductDao
import com.example.androiod_exam.DataClass.entity.CartItemEntity
import com.example.androiod_exam.DataClass.entity.OrderEntity
import com.example.androiod_exam.DataClass.entity.ProductEntity


@Database(entities = [ProductEntity::class, CartItemEntity::class, OrderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Bruker denne til å slette databasen og opprette på nytt ved endring av versjonsnummeret
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
