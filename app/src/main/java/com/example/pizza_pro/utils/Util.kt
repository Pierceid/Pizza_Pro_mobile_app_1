package com.example.pizza_pro.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pizza_pro.R
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.options.Satisfaction
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class Util {
    companion object {

        // returns whether the user is registering or not
        fun getIsRegistering(textView: TextView, activity: Activity): Boolean {
            return textView.text == activity.getString(R.string.register)
        }

        // returns whether the password is visible or not
        fun getVisibilityOfPassword(imageView: ImageView, context: Context): Boolean {
            return (imageView.drawable.constantState?.hashCode() == ContextCompat.getDrawable(
                context, R.drawable.ic_show
            )?.constantState?.hashCode())
        }

        // changes visibility of password
        fun changeVisibilityOfPassword(
            isPasswordVisible: Boolean, textInputEditText: TextInputEditText, imageView: ImageView
        ) {
            imageView.setImageResource(if (isPasswordVisible) R.drawable.ic_show else R.drawable.ic_hide)
            textInputEditText.transformationMethod =
                if (isPasswordVisible) null else PasswordTransformationMethod()
        }

        // changes visibility of a text input layout and radio group
        fun changeVisibilityOfAccountFields(
            isRegistering: Boolean, textInputLayout: TextInputLayout, radioGroup: RadioGroup
        ) {
            textInputLayout.visibility = if (isRegistering) View.VISIBLE else View.INVISIBLE
            radioGroup.visibility = if (isRegistering) View.VISIBLE else View.INVISIBLE
        }

        // swaps texts of 2 text views
        fun swapTextViews(textView1: TextView, textView2: TextView) {
            val temp = textView2.text
            textView2.text = textView1.text
            textView1.text = temp
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

        // navigates to a child fragment
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


        // creates an alert dialog
        fun createAlertDialog(
            activity: Activity,
            type: String? = null,
            runnable: Runnable = Runnable { },
            layoutInflater: LayoutInflater? = null,
            parentView: ConstraintLayout? = null
        ) {
            createToast(activity, true)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

            val builder = AlertDialog.Builder(activity)
            val message = when (type) {
                "order" -> activity.getString(R.string.place_order)
                "feedback" -> activity.getString(R.string.share_feedback)
                "history" -> activity.getString(R.string.clear_history)
                else -> activity.getString(R.string.exit_app)
            }
            builder.setMessage(message)
            builder.setPositiveButton("Yes") { _, _ ->
                if (type == null) {
                    createToast(activity, false)
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    activity.finish()
                } else {
                    val text = when (type) {
                        "order" -> activity.getString(R.string.ordered_successfully)
                        "feedback" -> activity.getString(R.string.sent_successfully)
                        "history" -> activity.getString(R.string.history_has_been_cleared)
                        else -> ""
                    }
                    createPopUpWindow(text, layoutInflater, parentView)
                    runnable.run()
                }
            }
            builder.setNegativeButton("No") { dialog, _ ->
                createToast(activity, false)
                dialog.dismiss()
            }
            builder.show()
        }

        // creates pop up window
        fun createPopUpWindow(
            text: String,
            layoutInflater: LayoutInflater?,
            parentView: ConstraintLayout?,
            konfettiView: KonfettiView? = null
        ) {
            if (layoutInflater == null || parentView == null) return

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
            val runnable = Runnable {
                message.text = text
                progressBar.visibility = View.GONE
                check.visibility = View.VISIBLE
                btnOk.visibility = View.VISIBLE
            }
            getHandler(runnable)

            btnOk.setOnClickListener {
                popupWindow.dismiss()
                val party = Party(emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(50))
                konfettiView?.start(party)
            }
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
        }

        // creates toast message
        fun createToast(activity: Activity, willBeLocked: Boolean) {
            val message =
                if (willBeLocked) activity.getString(R.string.locked)
                else activity.getString(R.string.unlocked)
            Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        // delays tasks by some time
        private fun getHandler(runnable: Runnable = Runnable { }, delay: Long = 2500L) {
            Handler().postDelayed(runnable, delay)
        }
    }
}


