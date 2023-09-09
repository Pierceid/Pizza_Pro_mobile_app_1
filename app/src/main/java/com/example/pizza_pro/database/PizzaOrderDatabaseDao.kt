package com.example.pizza_pro.database

import androidx.room.*

@Dao
interface PizzaOrderDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(pizzaOrder: PizzaOrder)

    @Delete
    suspend fun deleteOrder(pizzaOrder: PizzaOrder)

    @Query("SELECT * from pizza_order_table WHERE id = :key")
    fun getOrder(key: Long): PizzaOrder?

    @Query("SELECT * from pizza_order_table ORDER BY id DESC LIMIT 1")
    fun getRecentOrder(): PizzaOrder?

    @Query("SELECT * FROM pizza_order_table ORDER BY id ASC")
    fun getAllOrders(): MutableList<PizzaOrder>
}