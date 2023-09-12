package com.example.pizza_pro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {
    @Upsert
    suspend fun upsertOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE from pizza_order_table")
    suspend fun clearAllOrders()

    @Query("SELECT * FROM pizza_order_table WHERE name LIKE '%' || :regex || '%' ORDER BY id DESC")
    fun getFilteredOrders(regex: String): LiveData<MutableList<Order>>

    @Query("SELECT * FROM pizza_order_table ORDER BY id DESC")
    fun getAllOrders(): LiveData<MutableList<Order>>
}