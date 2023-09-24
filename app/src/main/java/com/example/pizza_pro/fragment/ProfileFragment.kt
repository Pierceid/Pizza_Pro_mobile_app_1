package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pizza_pro.databinding.FragmentProfileBinding
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.utils.Util

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var location: String
    private lateinit var gender: Gender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            name = it.getString("name").toString()
            email = it.getString("email").toString()
            password = it.getString("password").toString()
            location = it.getString("location").toString()
            gender = it.getSerializable("gender") as Gender
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { requireFragmentManager().popBackStack() }
        updateProfile()
    }

    // updates profile fragment
    private fun updateProfile() {
        binding.name = name
        binding.email = email
        binding.password = password
        binding.location = location
        binding.gender = gender.toString()
        Util.setProfileIcon(gender, binding.ivProfile)
    }
}