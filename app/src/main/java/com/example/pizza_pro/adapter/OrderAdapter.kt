package com.example.pizza_pro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.database.Order
import com.example.pizza_pro.database.OrderViewModel

class OrderAdapter(private val orderViewModel: OrderViewModel) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val orders: MutableList<Order> = mutableListOf()

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.tv_header)
        val body: TextView = itemView.findViewById(R.id.tv_body)

        private val xButton: ImageView = itemView.findViewById(R.id.btn_x)

        init {
            xButton.setOnClickListener {
                val position = adapterPosition
                val order = orders[position]
                orderViewModel.removeOrder(order)
                notifyItemRemoved(position)
            }
        }
    }

    // creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        )
    }

    // binds data from orders to views
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.header.text = String.format("%d. %s", order.id, order.userInfo.name)
        holder.body.text = String.format(
            "Email: %s\nTime: %s\nPlace: %s\nPurchase: %d (%s)",
            order.userInfo.email,
            order.time,
            order.place,
            order.items,
            order.cost
        )
    }

    // returns number of orders
    override fun getItemCount(): Int = orders.size

    // updates the list of orders
    fun initOrders(newList: MutableList<Order>) {
        val removedItems = orders.size
        val insertedItems = newList.size

        orders.clear()
        orders.addAll(newList)

        if (removedItems > 0) notifyItemRangeRemoved(0, removedItems)
        if (insertedItems > 0) notifyItemRangeInserted(0, insertedItems)
    }
}