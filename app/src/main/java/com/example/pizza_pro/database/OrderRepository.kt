package com.example.pizza_pro.database

import androidx.lifecycle.LiveData

class OrderRepository(private val dao: OrderDao) {

    var allOrders: LiveData<MutableList<Order>> = dao.getAllOrders()

    suspend fun addOrder(order: Order) = dao.upsertOrder(order)

    suspend fun removeOrder(order: Order) = dao.deleteOrder(order)

    suspend fun clearAllOrders() = dao.clearAllOrders()

    suspend fun getOrder(email: String) = dao.getOrder(email)

    fun getFilteredOrders(regex: String) = dao.getFilteredOrders(regex)
}