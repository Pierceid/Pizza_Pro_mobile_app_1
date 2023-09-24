package com.example.pizza_pro

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pizza_pro.databinding.ActivityMainBinding
import com.example.pizza_pro.utils.Util

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        setContentView(binding.root)
    }


    @Deprecated("Deprecated in Java",
        ReplaceWith("Util.createAlertDialog(this)", "com.example.pizza_pro.utils.Util")
    )
    override fun onBackPressed() {
        Util.createAlertDialog(this)
    }
}

