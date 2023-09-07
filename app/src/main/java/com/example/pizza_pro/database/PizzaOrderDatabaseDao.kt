package com.example.pizza_pro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PizzaOrderDatabaseDao {
    @Upsert
    suspend fun upsert(pizzaOrder: PizzaOrder)

    @Delete
    suspend fun delete(pizzaOrder: PizzaOrder)

    @Query("SELECT * from pizza_order_table WHERE id = :key")
    suspend fun get(key: Long): PizzaOrder?

    @Query("SELECT * from pizza_order_table ORDER BY id DESC LIMIT 1")
    suspend fun getRecent(): PizzaOrder?

    @Query("DELETE from pizza_order_table")
    suspend fun clear()

    @Query("SELECT * FROM pizza_order_table ORDER BY id ASC")
    fun getAll(): LiveData<List<PizzaOrder>>
}