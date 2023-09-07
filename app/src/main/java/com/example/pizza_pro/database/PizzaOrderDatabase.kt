package com.example.pizza_pro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PizzaOrder::class], version = 1, exportSchema = false)
abstract class PizzaOrderDatabase : RoomDatabase() {

    abstract val pizzaOrderDatabaseDao: PizzaOrderDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: PizzaOrderDatabase? = null

        fun getInstance(context: Context): PizzaOrderDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PizzaOrderDatabase::class.java,
                        "pizza_order_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}