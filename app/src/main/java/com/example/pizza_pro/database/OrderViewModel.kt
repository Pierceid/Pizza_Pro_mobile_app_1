package com.example.pizza_pro.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrderRepository
    var orders: LiveData<MutableList<Order>>
    var userOrder: Order?

    init {
        val dao = OrderDatabase.getDatabase(application).dao
        repository = OrderRepository(dao)
        orders = repository.allOrders
        userOrder = null
    }

    fun addOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

    fun removeOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOrder(order)
        }
    }

    fun clearAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllOrders()
        }
    }

    fun getUserOrder(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userOrder = if (email.isNotEmpty()) repository.getOrder(email) else null
        }
    }

    fun filterOrders(regex: String) {
        orders = if (regex.isNotEmpty()) repository.getFilteredOrders(regex) else repository.allOrders
    }
}