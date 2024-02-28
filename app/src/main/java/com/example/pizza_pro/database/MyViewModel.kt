package com.example.pizza_pro.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MyRepository
    var users: LiveData<MutableList<User>>
    var orders: LiveData<MutableList<Order>>
    var user: User?

    init {
        val dao = MyDatabase.getDatabase(application).dao
        repository = MyRepository(dao)
        orders = repository.allOrders
        users = repository.allUsers
        user = null
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrder(order)
        }
    }

    fun removeUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeUser(user)
        }
    }

    fun removeOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeOrder(order)
        }
    }

    fun clearAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllUsers()
        }
    }

    fun clearAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllOrders()
        }
    }

    fun getUser(name: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUser(name, email)
        }
    }

    fun getFilteredUsers(regex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            users =
                if (regex.isNotEmpty()) repository.getFilteredUsers(regex) else repository.allUsers
        }
    }

    fun getFilteredOrders(regex: String) {
        viewModelScope.launch(Dispatchers.IO) {
            orders =
                if (regex.isNotEmpty()) repository.getFilteredOrders(regex) else repository.allOrders
        }
    }
}