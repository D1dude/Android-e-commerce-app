package com.example.androiod_exam.DataClass.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androiod_exam.DataClass.entity.CartItemEntity

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun removeCartItem(cartItem: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE id = :cartItemId")
    suspend fun getCartItemById(cartItemId: Int): CartItemEntity?

    @Update
    suspend fun updateQuantity(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems()

}