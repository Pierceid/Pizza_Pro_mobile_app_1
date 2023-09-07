package com.example.pizza_pro.database

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class PizzaOrderVM(
    private val pizzaOrderDatabaseDao: PizzaOrderDatabaseDao, application: Application
) : AndroidViewModel(application) {

    private var recentOrder = MutableLiveData<PizzaOrder?>()

    init {
        initRecent()
    }

    private suspend fun getRecentFromDatabase(): PizzaOrder? {
        return pizzaOrderDatabaseDao.getRecent()
    }

    private fun initRecent() {
        viewModelScope.launch {
            recentOrder.value = getRecentFromDatabase()
        }
    }

    suspend fun upsert(pizzaOrder: PizzaOrder) {
        pizzaOrderDatabaseDao.upsert(pizzaOrder)
    }
}