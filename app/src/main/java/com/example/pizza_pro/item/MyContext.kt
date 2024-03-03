package com.example.pizza_pro.item

import android.app.Activity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.example.pizza_pro.database.MyViewModel

data class MyContext(
    val type: String,
    val activity: Activity,
    val layoutInflater: LayoutInflater,
    val myViewModel: MyViewModel? = null,
    val fragmentManager: FragmentManager? = null,
    val parentView: ConstraintLayout? = null
)