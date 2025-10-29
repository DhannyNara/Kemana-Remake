package com.ojol.kemana.merchant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private val orders: List<Order>,
    private val onAccept: (Order) -> Unit,
    private val onReject: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order, onAccept, onReject)
    }

    override fun getItemCount(): Int = orders.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.text_view_order_id)
        private val orderItemsTextView: TextView = itemView.findViewById(R.id.text_view_order_items)
        private val orderTotalTextView: TextView = itemView.findViewById(R.id.text_view_order_total)
        private val acceptButton: Button = itemView.findViewById(R.id.button_accept_order)
        private val rejectButton: Button = itemView.findViewById(R.id.button_reject_order)

        fun bind(order: Order, onAccept: (Order) -> Unit, onReject: (Order) -> Unit) {
            orderIdTextView.text = "Order #${order.id.substring(0, 5)}" // Tampilkan 5 karakter pertama ID
            orderItemsTextView.text = order.items.joinToString("\n") { "- ${it.name} x ${it.quantity}" }
            orderTotalTextView.text = "Total: Rp ${order.total}"

            acceptButton.setOnClickListener { onAccept(order) }
            rejectButton.setOnClickListener { onReject(order) }
        }
    }
}
