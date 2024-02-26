package com.example.pizza_pro.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pizza_pro.options.Gender

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) var userID: Long = 0L,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "gender") var gender: Gender = Gender.MALE
)

@Entity(tableName = "order_table")
data class Order(
    @PrimaryKey(autoGenerate = true) var orderID: Long = 0L,
    @Embedded("user") var user: User = User(),
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "items") var items: Int = 0,
    @ColumnInfo(name = "cost") var cost: String = ""
)
