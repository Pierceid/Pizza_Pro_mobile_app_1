package com.example.pizza_pro.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.pizza_pro.R
import com.example.pizza_pro.fragment.*
import com.example.pizza_pro.options.Gender

@Suppress("DEPRECATION")
class MyMenuProvider(
    private val activity: Activity,
    private val fragment: Fragment,
    private val fragmentManager: FragmentManager,
    private val navController: NavController
) : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_settings, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.mi_lock -> {
                val locked = (activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LOCKED)
                activity.requestedOrientation =
                    if (locked) ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    else ActivityInfo.SCREEN_ORIENTATION_LOCKED
                Util.createToast(activity, !locked)
                true
            }
            R.id.mi_profile -> {
                val bundle = if (fragment is AccountFragment) {
                    bundleOf(
                        "name" to "undefined",
                        "email" to "undefined",
                        "password" to "undefined",
                        "location" to "undefined",
                        "gender" to Gender.OTHER
                    )
                } else {
                    bundleOf(
                        "name" to fragment.requireArguments().getString("name").toString(),
                        "email" to fragment.requireArguments().getString("email").toString(),
                        "password" to fragment.requireArguments().getString("password").toString(),
                        "location" to fragment.requireArguments().getString("location").toString(),
                        "gender" to fragment.requireArguments().getSerializable("gender") as Gender
                    )
                }
                Util.navigateToFragment(fragmentManager, ProfileFragment(), bundle)
                true
            }
            R.id.mi_history -> {
                Util.navigateToFragment(fragmentManager, HistoryFragment())
                true
            }
            R.id.mi_aboutApp -> {
                Util.navigateToFragment(fragmentManager, AboutAppFragment())
                true
            }
            R.id.mi_logOut -> {
                Util.removeAdditionalFragment(fragmentManager)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                val action = when (fragment) {
                    is AccountFragment -> R.id.action_accountFragment_to_introFragment
                    is ShopFragment -> R.id.action_shopFragment_to_introFragment
                    is CartFragment -> R.id.action_cartFragment_to_introFragment
                    is FeedbackFragment -> R.id.action_feedbackFragment_to_introFragment
                    else -> 0
                }
                navController.navigate(action)
                true
            }
            else -> false
        }
    }
}