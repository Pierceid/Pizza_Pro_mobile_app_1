package com.example.pizza_pro.database

import androidx.lifecycle.LiveData

class OrderRepository(private val dao: OrderDao) {

    var allOrders: LiveData<MutableList<Order>> = dao.getAllOrders()

    suspend fun addOrder(order: Order) = dao.upsertOrder(order)

    suspend fun clearAllOrders() = dao.clearAllOrders()

    fun getFilteredOrders(regex: String) = dao.getFilteredOrders(regex)
}