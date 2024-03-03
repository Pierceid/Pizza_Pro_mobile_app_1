package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pizza_pro.database.MyViewModel
import com.example.pizza_pro.databinding.FragmentProfileBinding
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking
import java.util.*

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var myViewModel: MyViewModel
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var location: String
    private lateinit var gender: Gender

    private var action: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            name = it.getString("name").toString()
            email = it.getString("email").toString()
            password = it.getString("password").toString()
            location = it.getString("location").toString()
            gender = it.getSerializable("gender") as Gender
            action = it.getInt("action")
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
        navController = Navigation.findNavController(view)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        myViewModel.getUser(name, email)

        binding.btnDelete.setOnClickListener { createProfileAlertDialog() }
        binding.btnClose.setOnClickListener { requireFragmentManager().popBackStack() }
        updateProfile()
    }

    // creates an alert dialog for placing an order
    private fun createProfileAlertDialog() {
        if (myViewModel.user == null) {
            return
        }
        val runnable = {
            myViewModel.user?.let { user ->
                runBlocking { myViewModel.removeUser(user) }
            }
            Util.removeAdditionalFragment(requireFragmentManager())
            navController.navigate(action)
        }

        Util.createAlertDialog(
            requireActivity(), "remove_user", runnable,
            layoutInflater, binding.clProfile
        )
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