package com.example.pizza_pro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.database.PizzaOrder

class PizzaOrderAdapter(private val orders: MutableList<PizzaOrder>) : RecyclerView.Adapter<PizzaOrderAdapter.PizzaOrderViewHolder>(){
    inner class PizzaOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.tv_header)
        val body: TextView = itemView.findViewById(R.id.tv_body)
    }

    // creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaOrderViewHolder {
        return PizzaOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    // binds data from orders to views
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PizzaOrderViewHolder, position: Int) {
        val order = orders[position]

        holder.header.text = "${order.id} ${order.name}"
        holder.body.text = "Items: ${order.items}\nCost: ${order.cost}\n"
    }

    // returns number of orders
    override fun getItemCount(): Int = orders.size
}