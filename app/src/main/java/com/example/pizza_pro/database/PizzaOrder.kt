package com.example.pizza_pro.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pizza_order_table")
data class PizzaOrder(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,

    @ColumnInfo(name = "name") var name: String = "",

    @ColumnInfo(name = "email") var email: String = "",

    @ColumnInfo(name = "items") var items: Int = 0,

    @ColumnInfo(name = "cost") var cost: String = ""
)