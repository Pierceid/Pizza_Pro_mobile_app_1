package com.example.pizza_pro.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pizza_pro.R
import com.example.pizza_pro.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val bundle = bundleOf(
            "isLocked" to (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LOCKED)
        )
        binding.btnStart.setOnClickListener {
            navController.navigate(R.id.action_introFragment_to_accountFragment, bundle)
        }
    }
}
