package com.example.pizza_pro.database

import androidx.room.*

@Dao
interface PizzaOrderDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrder(pizzaOrder: PizzaOrder)

    @Delete
    fun deleteOrder(pizzaOrder: PizzaOrder)

    @Query("SELECT * from pizza_order_table WHERE id = :key")
    fun get(key: Long): PizzaOrder?

    @Query("SELECT * from pizza_order_table ORDER BY id DESC LIMIT 1")
    suspend fun getRecent(): PizzaOrder?

    @Query("SELECT * FROM pizza_order_table ORDER BY id ASC")
    fun getAll(): List<PizzaOrder>
}