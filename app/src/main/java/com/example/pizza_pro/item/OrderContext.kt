package com.example.pizza_pro.item

import android.app.Activity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pizza_pro.database.OrderViewModel

data class OrderContext (
    val orderViewModel: OrderViewModel,
    val activity: Activity,
    val layoutInflater: LayoutInflater,
    val parentView: ConstraintLayout
)