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

    @Query("DELETE FROM user_table")
    suspend fun clearAllUsers()

    @Query("DELETE FROM order_table")
    suspend fun clearAllOrders()

    @Query("SELECT * FROM user_table WHERE name = :name OR email = :email LIMIT 1")
    fun getUser(name: String, email: String): User?

    @Query("SELECT * FROM user_table WHERE name LIKE '%' || :regex || '%' ORDER BY id DESC")
    fun getFilteredUsers(regex: String): LiveData<MutableList<User>>

    @Query("SELECT * FROM order_table WHERE name LIKE '%' || :regex || '%' ORDER BY id DESC")
    fun getFilteredOrders(regex: String): LiveData<MutableList<Order>>

    @Query("SELECT * FROM user_table ORDER BY id DESC")
    fun getAllUsers(): LiveData<MutableList<User>>

    @Query("SELECT * FROM order_table ORDER BY id DESC")
    fun getAllOrders(): LiveData<MutableList<Order>>
}