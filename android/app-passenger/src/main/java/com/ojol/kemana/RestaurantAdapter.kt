package com.ojol.kemana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Data class untuk merepresentasikan sebuah restoran
data class Restaurant(val id: String, val name: String, val cuisine: String)

class RestaurantAdapter(
    private val restaurants: List<Restaurant>,
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
        holder.itemView.setOnClickListener { onItemClick(restaurant) }
    }

    override fun getItemCount(): Int = restaurants.size

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_restaurant_name)
        private val cuisineTextView: TextView = itemView.findViewById(R.id.text_view_cuisine_type)

        fun bind(restaurant: Restaurant) {
            nameTextView.text = restaurant.name
            cuisineTextView.text = restaurant.cuisine
        }
    }
}
