package com.example.pizza_pro.database

import androidx.lifecycle.LiveData

class MyRepository(private val dao: MyDao) {

    var allUsers: LiveData<MutableList<User>> = dao.getAllUsers()
    var allOrders: LiveData<MutableList<Order>> = dao.getAllOrders()

    suspend fun addUser(user: User) = dao.upsertUser(user)

    suspend fun addOrder(order: Order) = dao.upsertOrder(order)

    suspend fun removeUser(user: User) = dao.deleteUser(user)

    suspend fun removeOrder(order: Order) = dao.deleteOrder(order)

    suspend fun clearAllUsers() = dao.clearAllUsers()

    suspend fun clearAllOrders() = dao.clearAllOrders()

    fun getUser(name: String, email: String) = dao.getUser(name, email)

    fun getFilteredOrders(regex: String) = dao.getFilteredOrders(regex)

    fun getFilteredUsers(regex: String) = dao.getFilteredUsers(regex)
}