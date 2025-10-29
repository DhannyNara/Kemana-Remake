package com.ojol.kemana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class CartItem(val menuItem: MenuItem, val quantity: Int)

class CartAdapter(private val cartItems: List<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.text_view_item_name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.text_view_quantity)
        private val subtotalTextView: TextView = itemView.findViewById(R.id.text_view_subtotal)

        fun bind(cartItem: CartItem) {
            itemNameTextView.text = cartItem.menuItem.name
            quantityTextView.text = "x${cartItem.quantity}"
            val subtotal = cartItem.menuItem.price * cartItem.quantity
            subtotalTextView.text = "Rp $subtotal"
        }
    }
}
