package com.example.pizza_pro.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrderRepository
    val allOrders: LiveData<MutableList<Order>>

    init {
        val dao = OrderDatabase.getDatabase(application).dao
        repository = OrderRepository(dao)
        allOrders = repository.allOrders
    }

    fun addOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

    fun clearAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllOrders()
        }
    }
}