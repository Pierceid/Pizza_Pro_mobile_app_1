package com.example.pizza_pro.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pizza_pro.R
import com.example.pizza_pro.adapter.OrderAdapter
import com.example.pizza_pro.database.MyViewModel
import com.example.pizza_pro.databinding.FragmentHistoryBinding
import com.example.pizza_pro.item.OrderContext
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking
import java.util.*

@Suppress("DEPRECATION")
class HistoryFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        val orderContext =
            OrderContext(myViewModel, requireActivity(), layoutInflater, binding.clHistory)
        adapter = OrderAdapter(orderContext)
        binding.rvOrders.adapter = adapter

        listOf(
            binding.ivSearch,
            binding.ivCross,
            binding.btnClose,
            binding.btnClear
        ).forEach { it.setOnClickListener(this) }

        binding.etSearchBar.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) updateHistory() }

        updateHistory()
    }

    // saves data in case of rotating screen or exiting app
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("regex", binding.etSearchBar.text.toString())
    }

    // restores data from the saved state
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.etSearchBar.setText(savedInstanceState.getString("regex").toString())
            updateHistory()
        }
    }

    // handles on click methods
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_close -> requireFragmentManager().popBackStack()
            R.id.btn_clear -> createHistoryAlertDialog()
            R.id.iv_search -> updateHistory()
            R.id.iv_cross -> {
                binding.etSearchBar.setText("")
                updateHistory()
            }
        }
    }

    // creates an alert dialog for placing an order
    private fun createHistoryAlertDialog() {
        val runnable = { runBlocking { myViewModel.clearAllOrders() } }
        Util.createAlertDialog(
            requireActivity(),
            "clear_history",
            runnable,
            layoutInflater,
            binding.clHistory
        )
    }

    // updates history fragment
    private fun updateHistory() {
        val regex = binding.etSearchBar.text.toString()
        myViewModel.getFilteredOrders(regex.trim())
        myViewModel.orders.observe(viewLifecycleOwner) { newOrders ->
            adapter.initOrders(newOrders)
        }
        binding.etSearchBar.clearFocus()
    }
}