package com.example.pizza_pro.fragment

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
import com.example.pizza_pro.utils.Util
import java.text.NumberFormat

@Suppress("DEPRECATION")
class CartFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var navController: NavController
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderedPizzas: MutableList<Pizza>
    private lateinit var adapter: PizzaAdapter
    private var itemCount: Int = 0
    private var totalCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderedPizzas =
            requireArguments().getParcelableArrayList<Pizza>("selectedItems") as MutableList<Pizza>
        adapter = PizzaAdapter(childFragmentManager, orderedPizzas)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnApply.setOnClickListener(this)
        binding.btnOrder.setOnClickListener(this)
        binding.btnShop.setOnClickListener(this)
        binding.btnFeedback.setOnClickListener(this)

        (activity as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        navController = Navigation.findNavController(view)
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        binding.rvOrderedPizzas.adapter = adapter
        calculateCosts()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_profile -> {
                val bundle = bundleOf(
                    "name" to requireArguments().getString("name").toString(),
                    "email" to requireArguments().getString("email").toString(),
                    "password" to requireArguments().getString("password").toString(),
                    "location" to requireArguments().getString("location").toString(),
                    "gender" to requireArguments().getSerializable("gender") as Gender
                )
                Util.navigateToFragment(requireFragmentManager(), ProfileFragment(), bundle)
                true
            }
            R.id.mi_aboutApp -> {
                Util.navigateToFragment(requireFragmentManager(), AboutAppFragment())
                true
            }
            R.id.mi_history -> {
                Util.navigateToFragment(requireFragmentManager(), HistoryFragment())
                true
            }
            R.id.mi_logOut -> {
                navController.navigate(R.id.action_cartFragment_to_introFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    // handles on click methods
    override fun onClick(v: View?) {
        val bundle = bundleOf(
            "name" to requireArguments().getString("name").toString(),
            "email" to requireArguments().getString("email").toString(),
            "password" to requireArguments().getString("password").toString(),
            "location" to requireArguments().getString("location").toString(),
            "gender" to requireArguments().getSerializable("gender") as Gender,
            "orderedItems" to orderedPizzas as ArrayList<out Parcelable>,
        )
        when (v!!.id) {
            R.id.btn_apply -> updateCart()
            R.id.btn_order -> {
                updateCart()
                if (orderedPizzas.size == 0) return
                onAttach(requireContext())
                createOrderPopUpWindow()
                onDetach()
            }
            R.id.btn_shop -> navController.navigate(
                R.id.action_cartFragment_to_shopFragment, bundle
            )
            R.id.btn_feedback -> navController.navigate(
                R.id.action_cartFragment_to_feedbackFragment, bundle
            )
        }
    }

    // creates pop up window for "order" action
    private fun createOrderPopUpWindow() {
        Util.createPopUpWindow(
            getString(R.string.ordered_successfully), layoutInflater, binding.clCart
        )

        insertDataIntoDatabase()

        val runnable = {
            orderedPizzas.clear()
            adapter = PizzaAdapter(childFragmentManager, orderedPizzas)
            binding.rvOrderedPizzas.adapter = adapter
            updateCart()
        }
        Util.getHandler(runnable)
    }

    // inserts order into database
    private fun insertDataIntoDatabase() {
        val order = Order(
            name = requireArguments().getString("name").toString(),
            email = requireArguments().getString("email").toString(),
            items = itemCount,
            cost = NumberFormat.getCurrencyInstance().format(totalCost)
        )
        orderViewModel.addOrder(order)
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
        for (pizza in orderedPizzas) {
            itemsCost += (pizza.cost * pizza.count)
            itemCount += pizza.count
        }
        val deliveryCost = orderedPizzas.takeIf { it.isEmpty() }?.let { 0.0 } ?: 5.0
        totalCost = itemsCost + deliveryCost

        binding.itemsCost = NumberFormat.getCurrencyInstance().format(itemsCost)
        binding.deliveryCost = NumberFormat.getCurrencyInstance().format(deliveryCost)
        binding.totalCost = NumberFormat.getCurrencyInstance().format(totalCost)
    }
}