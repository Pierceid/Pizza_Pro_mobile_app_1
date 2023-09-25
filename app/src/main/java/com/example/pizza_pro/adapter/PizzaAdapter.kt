package com.example.pizza_pro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pizza_pro.R
import com.example.pizza_pro.fragment.DetailFragment
import com.example.pizza_pro.item.Pizza
import com.example.pizza_pro.utils.Util
import java.text.NumberFormat
import java.util.*

class PizzaAdapter(
    private val fragmentManager: FragmentManager, private val pizzas: MutableList<Pizza>
) : RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>() {

    // for recycle view behaviour
    inner class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pizza: ImageView = itemView.findViewById(R.id.iv_pizza)
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val count: TextView = itemView.findViewById(R.id.tv_itemCount)
        val cost: TextView = itemView.findViewById(R.id.tv_cost)

        private val plusButton: ImageView = itemView.findViewById(R.id.iv_plus)
        private val minusButton: ImageView = itemView.findViewById(R.id.iv_minus)
        private val pizzaName: TextView = itemView.findViewById(R.id.tv_name)

        init {
            plusButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    pizzas[position].count++
                    notifyItemChanged(position)
                }
            }

            minusButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && pizzas[position].count > 0) {
                    pizzas[position].count--
                    notifyItemChanged(position)
                }
            }

            pizzaName.setOnClickListener {
                val pizza = getPizza(pizzaName)
                val bundle = bundleOf(
                    "imageSource" to pizza?.imageSource,
                    "name" to pizza?.name,
                    "rating" to pizza?.rating,
                    "time" to pizza?.time,
                    "calories" to pizza?.calories,
                    "description" to pizza?.description,
                )
                Util.navigateToFragment(fragmentManager, DetailFragment(), bundle)
            }
        }
    }

    // creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        return PizzaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pizza, parent, false)
        )
    }

    // binds data from pizzas to views
    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val pizza = pizzas[position]

        holder.pizza.setImageResource(pizza.imageSource)
        holder.name.text = pizza.name
        holder.count.text = pizza.count.toString()
        holder.cost.text =
            String.format("Cost: %s", NumberFormat.getCurrencyInstance().format(pizza.cost))
    }

    // returns number of pizzas
    override fun getItemCount(): Int = pizzas.size

    // returns list of all pizzas
    fun getPizzas(): MutableList<Pizza> = pizzas

    // returns a pizza
    fun getPizza(textView: TextView): Pizza? = pizzas.find { it.name == textView.text.toString() }

    // returns list of filtered pizzas
    fun getFilteredPizzas(regex: String): MutableList<Pizza> =
        pizzas.filter {
            it.name!!.lowercase(Locale.getDefault()).contains(regex.lowercase(Locale.getDefault()))
        } as MutableList<Pizza>

    // returns list of filtered pizzas
    fun getSelectedPizzas(): MutableList<Pizza> =
        pizzas.filter { it.count > 0 } as MutableList<Pizza>

    // updates the list of pizzas
    fun initPizzas(newList: MutableList<Pizza>) {
        pizzas.clear()
        pizzas.addAll(newList)
    }
}