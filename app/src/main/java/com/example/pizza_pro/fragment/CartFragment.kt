package com.example.pizza_pro.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcelable
import android.text.format.DateFormat
import android.view.*
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.pizza_pro.R
import com.example.pizza_pro.adapter.PizzaAdapter
import com.example.pizza_pro.database.*
import com.example.pizza_pro.databinding.FragmentCartBinding
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.options.Gender
import com.example.pizza_pro.utils.MyMenuProvider
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
class CartFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var navController: NavController
    private lateinit var myViewModel: MyViewModel
    private lateinit var orderedPizzas: MutableList<Pizza>
    private lateinit var adapter: PizzaAdapter

    private var itemCount: Int = 0
    private var totalCost: Double = 0.0
    private var menuProvider: MenuProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val isLocked: Boolean
        requireArguments().let {
            orderedPizzas = it.getParcelableArrayList<Pizza>("orderedItems") as MutableList<Pizza>
            isLocked = it.getBoolean("isLocked")
        }

        adapter = PizzaAdapter(requireFragmentManager(), orderedPizzas)

        if (isLocked) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        navController = Navigation.findNavController(view)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        menuProvider =
            MyMenuProvider(requireActivity(), this, requireFragmentManager(), navController)
        requireActivity().addMenuProvider(menuProvider!!)

        listOf(
            binding.btnApply, binding.btnOrder, binding.btnShop, binding.btnFeedback
        ).forEach { it.setOnClickListener(this) }
        binding.rvOrderedPizzas.adapter = adapter

        calculateCosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider!!)
    }

    // handles on click methods
    override fun onClick(v: View?) {
        updateCart()
        val bundle = bundleOf(
            "name" to requireArguments().getString("name").toString(),
            "email" to requireArguments().getString("email").toString(),
            "password" to requireArguments().getString("password").toString(),
            "location" to requireArguments().getString("location").toString(),
            "gender" to requireArguments().getSerializable("gender") as Gender,
            "orderedItems" to orderedPizzas as ArrayList<out Parcelable>,
            "isLocked" to (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LOCKED)
        )
        when (v!!.id) {
            R.id.btn_order -> {
                if (orderedPizzas.size == 0) return
                createOrderAlertDialog()
            }
            R.id.btn_shop -> {
                navController.navigate(R.id.action_cartFragment_to_shopFragment, bundle)
            }
            R.id.btn_feedback -> {
                navController.navigate(R.id.action_cartFragment_to_feedbackFragment, bundle)
            }
        }
    }

    // creates an alert dialog for placing an order
    private fun createOrderAlertDialog() {
        val runnable = {
            insertOrderIntoDatabase()
            orderedPizzas.clear()
            adapter = PizzaAdapter(requireFragmentManager(), orderedPizzas)
            binding.rvOrderedPizzas.adapter = adapter
            updateCart()
        }
        Util.createAlertDialog(
            requireActivity(),
            "place_order",
            runnable,
            layoutInflater,
            binding.clCart,
            binding.konfettiView
        )
    }

    // inserts order into database
    private fun insertOrderIntoDatabase() {
        myViewModel.getUser(requireArguments().getString("email").toString())
        val existingUser = myViewModel.user

        // TODO fix get method in myDao (I guess)
        if (existingUser != null) {
            val order = Order(
                0,
                user = existingUser,
                time = DateFormat.format("d.M.yyyy (h:mm a)", System.currentTimeMillis())
                    .toString(),
                items = itemCount,
                cost = NumberFormat.getCurrencyInstance().format(totalCost)
            )
            runBlocking { myViewModel.addOrder(order) }
        }
    }

    // updates cart fragment
    private fun updateCart() {
        val iterator = orderedPizzas.iterator()
        var index = 0

        while (iterator.hasNext()) {
            val pizza = iterator.next()
            if (pizza.count == 0) {
                iterator.remove()
                adapter.notifyItemRemoved(index)
            } else {
                index++
            }
        }
        calculateCosts()
    }

    // calculates and updates costs of ordered items, services and total cost
    private fun calculateCosts() {
        var itemsCost = 0.0
        itemCount = 0
        orderedPizzas.forEach {
            itemsCost += it.cost * it.count
            itemCount += it.count
        }
        val deliveryCost = orderedPizzas.takeIf { it.isEmpty() }?.let { 0.0 } ?: 5.0
        totalCost = itemsCost + deliveryCost

        binding.itemsCost = NumberFormat.getCurrencyInstance().format(itemsCost)
        binding.deliveryCost = NumberFormat.getCurrencyInstance().format(deliveryCost)
        binding.totalCost = NumberFormat.getCurrencyInstance().format(totalCost)
    }
}