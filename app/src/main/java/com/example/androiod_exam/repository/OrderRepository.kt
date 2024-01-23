package com.example.androiod_exam.repository

import com.example.androiod_exam.DataClass.dao.OrderDao
import com.example.androiod_exam.DataClass.entity.OrderEntity

class OrderRepository(private val orderDao: OrderDao) {

    suspend fun getOrders(): List<OrderEntity> {
        return orderDao.getOrders()
    }

    // suspend fun insertOrders(orders: List<OrderEntity>) { orderDao.insertOrders(orders) }

    suspend fun getOrdersByCartId(cartId: String): List<OrderEntity> {
        return orderDao.getOrdersByCartId(cartId)
    }
}