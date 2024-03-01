package com.example.pizza_pro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.database.Order
import com.example.pizza_pro.database.User
import com.example.pizza_pro.item.MyContext
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking

class HistoryAdapter(private val myContext: MyContext) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: MutableList<Any> = mutableListOf()

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val header: TextView = itemView.findViewById(R.id.tv_header)
        val body: TextView = itemView.findViewById(R.id.tv_body)

        private val xButton: ImageView = itemView.findViewById(R.id.btn_x)

        init {
            xButton.setOnClickListener {
                var alertType = ""
                var runnable = { }

                when (myContext.type) {
                    "users" -> {
                        alertType = "remove_user"
                        runnable = {
                            val position = adapterPosition
                            val user = items[position] as User
                            runBlocking { myContext.myViewModel.removeUser(user) }
                            notifyItemRemoved(position)
                        }
                    }
                    "orders" -> {
                        alertType = "cancel_order"
                        runnable = {
                            val position = adapterPosition
                            val order = items[position] as Order
                            runBlocking { myContext.myViewModel.removeOrder(order) }
                            notifyItemRemoved(position)
                        }
                    }
                    else -> {}
                }

                Util.createAlertDialog(
                    myContext.activity,
                    alertType,
                    runnable,
                    myContext.layoutInflater,
                    myContext.parentView
                )
            }
        }
    }

    // creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    // binds data from users/orders to views
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        when (myContext.type) {
            "users" -> {
                val user = items[position] as User
                holder.header.text = String.format("%d. %s", user.id, user.name)
                holder.body.text = String.format(
                    "Email: %s\nPassword: %s\nGender %s",
                    user.email, user.password.map { '*' }.joinToString(""), user.gender
                )
            }
            "orders" -> {
                val order = items[position] as Order
                holder.header.text = String.format("%d. %s", order.id, order.name)
                holder.body.text = String.format(
                    "Time: %s\nPlace: %s\nPurchase: %d (%s)",
                    order.time, order.place, order.items, order.cost
                )
            }
            else -> {}
        }
    }

    // returns number of items
    override fun getItemCount(): Int = items.size

    // updates the list of items
    fun initItems(newList: MutableList<Any>) {
        val removedItems = items.size
        val insertedItems = newList.size

        items.clear()
        items.addAll(newList)

        if (removedItems > 0) notifyItemRangeRemoved(0, removedItems)
        if (insertedItems > 0) notifyItemRangeInserted(0, insertedItems)
    }
}