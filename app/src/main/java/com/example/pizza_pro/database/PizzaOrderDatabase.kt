package com.example.pizza_pro.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PizzaOrder::class], version = 1, exportSchema = false)
abstract class PizzaDatabase : RoomDatabase() {
    abstract val pizzaDatabaseDao: PizzaDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: PizzaDatabase? = null

        fun getInstance(context: Context): PizzaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {}

                return instance
            }

        }
    }
}