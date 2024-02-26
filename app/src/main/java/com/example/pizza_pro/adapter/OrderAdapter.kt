package com.example.pizza_pro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.database.Order
import com.example.pizza_pro.item.OrderContext
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking

class OrderAdapter(private val orderContext: OrderContext) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val users: MutableList<Order> = mutableListOf()

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.tv_header)
        val body: TextView = itemView.findViewById(R.id.tv_body)

        private val xButton: ImageView = itemView.findViewById(R.id.btn_x)

        init {
            xButton.setOnClickListener {
                val runnable = {
                    val position = adapterPosition
                    val order = users[position]
                    runBlocking { orderContext.myViewModel.removeOrder(order) }
                    notifyItemRemoved(position)
                }
                Util.createAlertDialog(
                    orderContext.activity,
                    "remove_order",
                    runnable,
                    orderContext.layoutInflater,
                    orderContext.parentView
                )
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
        val order = users[position]

        holder.header.text = String.format("%d. %s", order.orderID, order.user.name)
        holder.body.text = String.format(
            "Email: %s\nPlace: %s\nPurchase: %d (%d)",
            order.user.email, order.user.location, order.items, order.cost
        )
    }

    // returns number of orders
    override fun getItemCount(): Int = users.size

    // updates the list of orders
    fun initOrders(newList: MutableList<Order>) {
        val removedItems = users.size
        val insertedItems = newList.size

        users.clear()
        users.addAll(newList)

        if (removedItems > 0) notifyItemRangeRemoved(0, removedItems)
        if (insertedItems > 0) notifyItemRangeInserted(0, insertedItems)
    }
}