package com.example.pizza_pro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyDao {
    @Upsert
    suspend fun upsertUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
    @Upsert
    suspend fun upsertOrder(order: Order)
    @Delete
    suspend fun deleteOrder(order: Order)
    @Query("DELETE FROM order_table")
    suspend fun clearAllOrders()
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUser(email: String): User?
    @Query("SELECT * FROM user_table WHERE name LIKE '%' || :regex || '%' ORDER BY userID DESC")
    fun getFilteredUsers(regex: String): LiveData<MutableList<User>>
    @Query("SELECT * FROM order_table WHERE time LIKE '%' || :regex || '%' ORDER BY orderID DESC")
    fun getFilteredOrders(regex: String): LiveData<MutableList<Order>>
    @Query("SELECT * FROM user_table ORDER BY userID DESC")
    fun getAllUsers(): LiveData<MutableList<User>>
    @Query("SELECT * FROM order_table ORDER BY orderID DESC")
    fun getAllOrders(): LiveData<MutableList<Order>>
}