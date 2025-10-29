package com.ojol.kemana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

// Data class untuk merepresentasikan item menu
data class MenuItem(val id: String, val name: String, val price: Double)

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val onAddItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem)
        holder.addButton.setOnClickListener { onAddItemClick(menuItem) }
    }

    override fun getItemCount(): Int = menuItems.size

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_item_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.text_view_item_price)
        val addButton: Button = itemView.findViewById(R.id.button_add_to_cart)

        fun bind(menuItem: MenuItem) {
            nameTextView.text = menuItem.name
            // Format harga ke dalam format Rupiah
            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            priceTextView.text = format.format(menuItem.price)
        }
    }
}
