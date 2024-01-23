package com.example.androiod_exam.DataClass.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androiod_exam.DataClass.entity.OrderEntity


@Dao
interface OrderDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders:List <OrderEntity>)

    @Query("SELECT * FROM orders")
    suspend fun getOrders(): List<OrderEntity>
    @Query("SELECT * FROM orders WHERE cartId = :cartId")
    suspend fun getOrdersByCartId(cartId: String): List<OrderEntity>

    @Query("SELECT MAX(cartId) FROM orders")
    suspend fun getMaxOrderCartId(): String?
}

