package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pizza_pro.R
import com.example.pizza_pro.databinding.FragmentAccountBinding
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.utils.Util

@Suppress("DEPRECATION")
class AccountFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var navController: NavController
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var location: String
    private lateinit var gender: Gender
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        navController = Navigation.findNavController(view)
        binding.btnEye.setOnClickListener(this)
        binding.rbMale.setOnClickListener(this)
        binding.rbFemale.setOnClickListener(this)
        binding.rbOther.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
    }

    // handles on click methods
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_eye -> {
                isPasswordVisible = !isPasswordVisible
                updateAccount()
            }
            R.id.rb_male, R.id.rb_female, R.id.rb_other -> updateAccount()
            R.id.btn_next -> {
                if (checkInput()) {
                    val bundle = bundleOf(
                        "name" to name,
                        "email" to email,
                        "password" to password,
                        "location" to location,
                        "gender" to gender
                    )
                    navController.navigate(
                        R.id.action_accountFragment_to_shopFragment, bundle
                    )
                }
            }
            R.id.btn_cancel -> requireActivity().onBackPressed()
        }
    }

    // saves data in case of rotating screen or exiting app
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getInput()
        outState.putString("name", name)
        outState.putString("email", email)
        outState.putString("password", password)
        outState.putString("location", location)
        outState.putSerializable("gender", gender)
        outState.putBoolean("isPasswordVisible", isPasswordVisible)
    }

    // restores data from the saved state
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.inputName.setText(savedInstanceState.getString("name").toString())
            binding.inputEmail.setText(savedInstanceState.getString("email").toString())
            binding.inputPassword.setText(savedInstanceState.getString("password").toString())
            binding.inputLocation.setText(savedInstanceState.getString("location").toString())
            updateAccount(
                savedInstanceState.getBoolean("isPasswordVisible"),
                savedInstanceState.getSerializable("gender") as Gender
            )
        }
    }

    // updates account fragment
    private fun updateAccount(
        newIsPasswordVisible: Boolean = isPasswordVisible,
        newGender: Gender = Util.getGenderFromRadioGroup(binding.rgGenderOptions)
    ) {
        isPasswordVisible = newIsPasswordVisible
        Util.changeVisibilityOfPassword(isPasswordVisible, binding.inputPassword, binding.btnEye)
        gender = newGender
        Util.checkGenderRadioButton(gender, binding.rgGenderOptions)
    }

    // validates user's input
    private fun checkInput(): Boolean {
        getInput()
        binding.inputName.error = if (name.isEmpty()) getString(R.string.invalid_username) else null
        binding.inputEmail.error = if (email.isEmpty()) getString(R.string.invalid_email) else null
        binding.inputPassword.error =
            if (password.length < 6) getString(R.string.invalid_password) else null
        binding.inputLocation.error =
            if (location.isEmpty()) getString(R.string.invalid_location) else null

        return name.isNotEmpty() && email.isNotEmpty() && password.length > 5 && location.isNotEmpty()
    }

    // assigns values to attributes
    private fun getInput() {
        name = binding.inputName.text.toString()
        email = binding.inputEmail.text.toString()
        password = binding.inputPassword.text.toString()
        location = binding.inputLocation.text.toString()
        gender = Util.getGenderFromRadioGroup(binding.rgGenderOptions)
        isPasswordVisible =
            (binding.btnEye.drawable.constantState?.hashCode() == ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_show
            )?.constantState?.hashCode())
    }
}