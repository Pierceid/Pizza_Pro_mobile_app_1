package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pizza_pro.databinding.FragmentAboutAppBinding

private const val VERSION: String = "Pizza Pro - Version 1.0"
private const val COPYRIGHT: String = "Copyright © 2023 Erik Mešina.\nAll rights reserved."
private const val DESCRIPTION: String =
    "Welcome to Pizza Pro, the ultimate pizza ordering app designed to satisfy your cravings with convenience and flavor."
private const val HIGHLIGHTS: String =
    "App Highlights:\n" + "\uD83C\uDF55 Explore Our Menu: Browse a delectable assortment of pizzas.\n" + "\uD83D\uDED2 Easy Ordering: Customize and place your order effortlessly.\n" + "\uD83D\uDE80 Fast Delivery: Enjoy prompt delivery to your doorstep."
private const val SUPPORT: String =
    "Questions or suggestions?\nReach out to our support team at support@pizzapro.com."
private const val THANKS: String = "Thank you for choosing Pizza Pro for your pizza desires!"

@Suppress("DEPRECATION")
class AboutAppFragment : Fragment() {

    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutAppBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { requireFragmentManager().popBackStack() }
        updateAboutApp()
    }

    // updates about app fragment
    private fun updateAboutApp() {
        binding.version = VERSION
        binding.copyright = COPYRIGHT
        binding.description = DESCRIPTION
        binding.highlights = HIGHLIGHTS
        binding.support = SUPPORT
        binding.thanks = THANKS
    }
}