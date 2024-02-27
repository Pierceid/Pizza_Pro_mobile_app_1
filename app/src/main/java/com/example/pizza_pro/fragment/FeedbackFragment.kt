package com.example.pizza_pro.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pizza_pro.R
import com.example.pizza_pro.databinding.FragmentFeedbackBinding
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.options.Satisfaction
import com.example.pizza_pro.utils.MyMenuProvider
import com.example.pizza_pro.utils.Util

@Suppress("DEPRECATION")
class FeedbackFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var navController: NavController
    private lateinit var orderedPizzas: MutableList<Pizza>

    private var satisfaction: Satisfaction = Satisfaction.GREAT
    private var thoughts: String = ""
    private var followUp: Boolean = true
    private var menuProvider: MenuProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val isLocked: Boolean
        requireArguments().let {
            orderedPizzas = it.getParcelableArrayList<Pizza>("orderedItems") as MutableList<Pizza>
            isLocked = it.getBoolean("isLocked")
        }

        if (isLocked) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        navController = Navigation.findNavController(view)
        menuProvider = MyMenuProvider(requireActivity(), this, requireFragmentManager(), navController)
        requireActivity().addMenuProvider(menuProvider!!)

        listOf(
            binding.btnSend,
            binding.btnDiscard,
            binding.btnCart,
            binding.scFollowUp
        ).forEach { it.setOnClickListener(this) }

        updateFeedback()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider!!)
    }

    // saves data in case of rotating screen or exiting app
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getInput()
        outState.putSerializable("satisfaction", satisfaction)
        outState.putString("thoughts", thoughts)
        outState.putBoolean("followUp", followUp)
    }

    // restores data from the saved state
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            updateFeedback(
                savedInstanceState.getSerializable("satisfaction") as Satisfaction,
                savedInstanceState.getString("thoughts").toString(),
                savedInstanceState.getBoolean("followUp")
            )
        }
    }

    // handles on click methods
    override fun onClick(v: View?) {
        val bundle = bundleOf(
            "name" to requireArguments().getString("name").toString(),
            "email" to requireArguments().getString("email").toString(),
            "password" to requireArguments().getString("password").toString(),
            "location" to requireArguments().getString("location").toString(),
            "gender" to requireArguments().getSerializable("gender") as Gender,
            "orderedItems" to requireArguments().getParcelableArrayList<Pizza>("orderedItems") as MutableList<Pizza>,
            "isLocked" to (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LOCKED)
        )
        when (v!!.id) {
            R.id.btn_send -> createFeedbackAlertDialog()
            R.id.btn_discard -> clearInput()
            R.id.sc_followUp -> {
                followUp = !followUp
                Util.changeSwitchState(requireContext(), followUp, binding.scFollowUp)
            }
            R.id.btn_cart -> {
                navController.navigate(R.id.action_feedbackFragment_to_cartFragment, bundle)
            }
        }
    }

    // updates feedback fragment
    private fun updateFeedback(
        newSatisfaction: Satisfaction = satisfaction,
        newThoughts: String = thoughts,
        newFollowUp: Boolean = followUp
    ) {
        satisfaction = newSatisfaction
        Util.checkSatisfactionRadioButton(satisfaction, binding.rgSatisfactionOptions)
        thoughts = newThoughts
        binding.etThoughts.setText(thoughts)
        followUp = newFollowUp
        Util.changeSwitchState(requireContext(), followUp, binding.scFollowUp)
    }

    // assigns values to attributes
    private fun getInput() {
        satisfaction = Util.getSatisfactionFromRadioGroup(binding.rgSatisfactionOptions)
        thoughts = binding.etThoughts.text.toString()
        followUp = binding.scFollowUp.isChecked
    }

    // resets values to attributes
    private fun clearInput() = updateFeedback(Satisfaction.GREAT, "", true)

    // creates an alert dialog for sharing feedback
    private fun createFeedbackAlertDialog() {
        val runnable = { clearInput() }
        Util.createAlertDialog(
            requireActivity(),
            "send_feedback",
            runnable,
            layoutInflater,
            binding.clFeedback,
            binding.konfettiView
        )
    }
}