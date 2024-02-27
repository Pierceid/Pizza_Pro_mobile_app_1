package com.example.pizza_pro.item

import android.app.Activity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pizza_pro.database.MyViewModel

data class MyContext (
    val myViewModel: MyViewModel,
    val activity: Activity,
    val layoutInflater: LayoutInflater,
    val parentView: ConstraintLayout,
    val type: String
)