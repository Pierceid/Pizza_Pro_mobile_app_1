package com.example.pizza_pro.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PizzaOrderVMFactory(
    private val dataSource: PizzaOrderDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PizzaOrderVM::class.java)) {
            return PizzaOrderVM(dataSource, application) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}