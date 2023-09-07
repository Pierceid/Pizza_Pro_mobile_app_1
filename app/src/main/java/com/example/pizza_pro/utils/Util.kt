package com.example.pizza_pro

import android.widget.ImageView
import android.widget.RadioGroup
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.options.Satisfaction

class Utils {
    companion object {
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
                Gender.MALE -> R.drawable.profile_male
                Gender.FEMALE -> R.drawable.profile_female
                else -> R.drawable.profile_other
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
    }
}


