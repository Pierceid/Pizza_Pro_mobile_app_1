package com.example.pizza_pro.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pizza_pro.options.Gender

@Entity(tableName = "pizza_order_table")
data class Order(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,

    @Embedded var userInfo: UserInfo = UserInfo(),

    @ColumnInfo(name = "time") var time: String = "",

    @ColumnInfo(name = "place") var place: String = "",

    @ColumnInfo(name = "items") var items: Int = 0,

    @ColumnInfo(name = "cost") var cost: String = ""
)

data class UserInfo(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var gender: Gender = Gender.MALE
)