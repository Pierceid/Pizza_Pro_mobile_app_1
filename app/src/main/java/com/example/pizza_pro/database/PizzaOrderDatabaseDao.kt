package com.example.pizza_pro.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PizzaDatabaseDao {
    @Insert
    fun insert(pizzaOrder: PizzaOrder)

    @Update
    fun update(pizzaOrder: PizzaOrder)

    @Query("SELECT * from pizza_order_table WHERE orderID = :key")
    fun get(key: Long): PizzaOrder?

    @Query("DELETE from pizza_order_table")
    fun clear()

    @Query("SELECT * from pizza_order_table ORDER BY orderID DESC LIMIT 1")
    fun getRecentOrder(): PizzaOrder?

    @Query("SELECT * FROM pizza_order_table ORDER BY orderID DESC")
    fun getAllOrders(): LiveData<List<PizzaOrder>>
}