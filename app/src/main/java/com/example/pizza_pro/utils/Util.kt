package com.example.pizza_pro.utils

import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pizza_pro.R
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.options.Satisfaction
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@Suppress("DEPRECATION")
class Util {
    companion object {

        // changes visibility of password
        fun changeVisibilityOfPassword(
            isPasswordVisible: Boolean, textInputEditText: TextInputEditText, imageView: ImageView
        ) {
            imageView.setImageResource(if (isPasswordVisible) R.drawable.ic_show else R.drawable.ic_hide)
            textInputEditText.transformationMethod =
                if (isPasswordVisible) null else PasswordTransformationMethod()
        }

        // changes visibility of name and gender field and swaps selected option (log in <-> register)
        fun changeVisibilityOfTextInputFields(
            isRegistering: Boolean,
            selectedEditText: TextView,
            unselectedEditText: TextView,
            textInputLayout: TextInputLayout,
            radioGroup: RadioGroup
        ) {
            val unselected = unselectedEditText.text
            unselectedEditText.text = selectedEditText.text
            selectedEditText.text = unselected

            textInputLayout.visibility = if (isRegistering) View.VISIBLE else View.INVISIBLE
            radioGroup.visibility = if (isRegistering) View.VISIBLE else View.INVISIBLE
        }

        // returns gender based on checked radio button
        fun getGenderFromRadioGroup(radioGroup: RadioGroup): Gender {
            return when (radioGroup.checkedRadioButtonId) {
                R.id.rb_male -> Gender.MALE
                R.id.rb_female -> Gender.FEMALE
                else -> Gender.OTHER
            }
        }

        // checks radio button based on gender
        fun checkGenderRadioButton(gender: Gender, radioGroup: RadioGroup) {
            val radioButtonId = when (gender) {
                Gender.MALE -> R.id.rb_male
                Gender.FEMALE -> R.id.rb_female
                else -> R.id.rb_other
            }
            radioGroup.check(radioButtonId)
        }

        // sets profile icon based on gender
        fun setProfileIcon(gender: Gender, imageView: ImageView) {
            val imageSource = when (gender) {
                Gender.MALE -> R.raw.profile_male
                Gender.FEMALE -> R.raw.profile_female
                else -> R.raw.profile_other
            }
            imageView.setImageResource(imageSource)
        }

        // returns satisfaction based on checked radio button
        fun getSatisfactionFromRadioGroup(radioGroup: RadioGroup): Satisfaction {
            return when (radioGroup.checkedRadioButtonId) {
                R.id.rb_great -> Satisfaction.GREAT
                R.id.rb_good -> Satisfaction.GOOD
                R.id.rb_decent -> Satisfaction.DECENT
                else -> Satisfaction.BAD
            }
        }

        // checks radio button based on satisfaction
        fun checkSatisfactionRadioButton(satisfaction: Satisfaction, radioGroup: RadioGroup) {
            val radioButtonId = when (satisfaction) {
                Satisfaction.GREAT -> R.id.rb_great
                Satisfaction.GOOD -> R.id.rb_good
                Satisfaction.DECENT -> R.id.rb_decent
                else -> R.id.rb_bad
            }
            radioGroup.check(radioButtonId)
        }

        // updates main list of pizzas based on some other list of pizzas
        fun updatePizzas(
            mainList: MutableList<Pizza>, otherList: MutableList<Pizza>
        ) {
            if (otherList.isNotEmpty()) {
                otherList.forEach { otherPizza ->
                    mainList.find { it.name == otherPizza.name }?.count = otherPizza.count
                }
            }
        }

        // removes the last fragment from backstack if its an additional fragment
        fun removeAdditionalFragment(fragmentManager: FragmentManager) {
            val lastTag = fragmentManager.fragments[fragmentManager.fragments.size - 1].tag
            val containerTag = fragmentManager.findFragmentById(R.id.fragmentContainer)?.tag
            if (lastTag == containerTag) fragmentManager.popBackStack()
        }

        // navigates to a fragment
        fun navigateToFragment(
            fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle? = null
        ) {
            val size = fragmentManager.fragments.size
            if (size > 1) {
                val currentTag = fragmentManager.fragments[size - 1].tag
                val previousTags = fragmentManager.fragments.subList(size - 2, size).map { it.tag }

                if (previousTags.contains(currentTag)) fragmentManager.popBackStack()
            }
            fragment.arguments = bundle
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null).commit()
        }

        // creates pop up window
        fun createPopUpWindow(
            text: String, layoutInflater: LayoutInflater, parentView: ConstraintLayout
        ) {
            val popupView = layoutInflater.inflate(R.layout.pop_up_window, parentView, false)
            val message = popupView.findViewById<TextView>(R.id.tv_message)
            val progressBar = popupView.findViewById<ProgressBar>(R.id.progressBar)
            val check = popupView.findViewById<ImageView>(R.id.iv_check)
            val btnOk = popupView.findViewById<Button>(R.id.btn_ok)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )
            // Delayed visibility changes
            val runnable = Runnable {
                message.text = text
                progressBar.visibility = View.GONE
                check.visibility = View.VISIBLE
                btnOk.visibility = View.VISIBLE
            }
            getHandler(runnable)
            btnOk.setOnClickListener {
                popupWindow.dismiss()
            }
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        }

        // delays tasks (runnable) by some time (delay)
        fun getHandler(runnable: Runnable, delay: Long = 2000L) {
            Handler().postDelayed(runnable, delay)
        }
    }
}


