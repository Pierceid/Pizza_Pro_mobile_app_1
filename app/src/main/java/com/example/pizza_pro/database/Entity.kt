package com.example.pizza_pro.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pizza_pro.options.Gender

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "gender") var gender: Gender = Gender.OTHER
)

@Entity(tableName = "order_table")
data class Order(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "place") var place: String = "",
    @ColumnInfo(name = "items") var items: Int = 0,
    @ColumnInfo(name = "cost") var cost: String = ""
)
