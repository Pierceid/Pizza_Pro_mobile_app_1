package com.example.pizza_pro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.database.Order
import com.example.pizza_pro.database.User
import com.example.pizza_pro.fragment.DetailFragment
import com.example.pizza_pro.item.MyContext
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.utils.Util
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.*

class MyAdapter<T>(private val myContext: MyContext) :
    RecyclerView.Adapter<MyAdapter<T>.HistoryViewHolder>() {

    private var items: MutableList<T> = mutableListOf()

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView? = itemView.findViewById(R.id.iv_pizza)
        val name: TextView? = itemView.findViewById(R.id.tv_name)
        val count: TextView? = itemView.findViewById(R.id.tv_itemCount)
        val cost: TextView? = itemView.findViewById(R.id.tv_cost)
        val header: TextView? = itemView.findViewById(R.id.tv_header)
        val body: TextView? = itemView.findViewById(R.id.tv_body)

        private val xButton: ImageView? = itemView.findViewById(R.id.btn_x)
        private val plusButton: ImageView? = itemView.findViewById(R.id.iv_plus)
        private val minusButton: ImageView? = itemView.findViewById(R.id.iv_minus)

        init {
            when (myContext.type) {
                "pizzas" -> {
                    plusButton?.setOnClickListener {
                        val pizza = items[adapterPosition] as Pizza
                        if (adapterPosition != RecyclerView.NO_POSITION && pizza.count < 10) {
                            pizza.count++
                            notifyItemChanged(adapterPosition)
                        }
                    }
                    minusButton?.setOnClickListener {
                        val pizza = items[adapterPosition] as Pizza
                        if (adapterPosition != RecyclerView.NO_POSITION && pizza.count > 0) {
                            pizza.count--
                            notifyItemChanged(adapterPosition)
                        }
                    }
                    image?.setOnClickListener {
                        val pizza = items[adapterPosition] as Pizza
                        openDetailFragment(pizza)
                    }
                    name?.setOnClickListener {
                        val pizza = items[adapterPosition] as Pizza
                        openDetailFragment(pizza)
                    }
                }
                "users" -> {
                    xButton?.setOnClickListener {
                        val runnable = {
                            val user = items[adapterPosition] as User
                            runBlocking { myContext.myViewModel?.removeUser(user) }
                            notifyItemRemoved(adapterPosition)
                        }
                        Util.createAlertDialog(
                            myContext.activity, "remove_user", runnable,
                            myContext.layoutInflater, myContext.parentView
                        )
                    }
                }
                "orders" -> {
                    xButton?.setOnClickListener {
                        val runnable = {
                            val order = items[adapterPosition] as Order
                            runBlocking { myContext.myViewModel?.removeOrder(order) }
                            notifyItemRemoved(adapterPosition)
                        }
                        Util.createAlertDialog(
                            myContext.activity, "cancel_order", runnable,
                            myContext.layoutInflater, myContext.parentView
                        )
                    }
                }
                else -> {}
            }
        }
    }

    // creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layout = if (myContext.type == "pizzas") R.layout.item_pizza else R.layout.item_history
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    // binds data from items to views
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        when (myContext.type) {
            "pizzas" -> {
                val pizza = items[position] as Pizza
                holder.image?.setImageResource(pizza.imageSource)
                holder.name?.text = pizza.name
                holder.count?.text = pizza.count.toString()
                holder.cost?.text =
                    String.format("Cost: %s", NumberFormat.getCurrencyInstance().format(pizza.cost))

            }
            "users" -> {
                val user = items[position] as User
                holder.header?.text = String.format("%d. %s", user.id, user.name)
                holder.body?.text = String.format(
                    "Email: %s\nPassword: %s\nGender %s",
                    user.email, user.password.map { '*' }.joinToString(""), user.gender
                )
            }
            "orders" -> {
                val order = items[position] as Order
                holder.header?.text = String.format("%d. %s", order.id, order.name)
                holder.body?.text = String.format(
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
    fun initItems(newList: MutableList<T>) {
        val removedItems = items.size
        val insertedItems = newList.size

        items.clear()
        items.addAll(newList)

        if (removedItems > 0) notifyItemRangeRemoved(0, removedItems)
        if (insertedItems > 0) notifyItemRangeInserted(0, insertedItems)
    }

    // returns list of filtered pizzas
    fun getFilteredPizzas(regex: String): MutableList<Pizza> {
        return items.asSequence().filterIsInstance<Pizza>().filter {
            it.name!!.lowercase(Locale.getDefault()).contains(regex.lowercase(Locale.getDefault()))
        }.toMutableList()
    }

    // returns list of filtered pizzas
    fun getSelectedPizzas(): MutableList<Pizza> {
        return items.asSequence().filterIsInstance<Pizza>().filter { it.count > 0 }.toMutableList()
    }

    // opens detail fragment of the selected pizza
    fun openDetailFragment(pizza: Pizza) {
        val bundle = bundleOf(
            "imageSource" to pizza.imageSource,
            "name" to pizza.name,
            "rating" to pizza.rating,
            "time" to pizza.time,
            "calories" to pizza.calories,
            "description" to pizza.description,
        )
        Util.navigateToFragment(myContext.fragmentManager!!, DetailFragment(), bundle)
    }
}