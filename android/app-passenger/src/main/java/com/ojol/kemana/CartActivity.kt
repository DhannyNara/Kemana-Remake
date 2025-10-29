package com.ojol.kemana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utsman.kemana.remote.driver.Model
import com.utsman.kemana.remote.merchant.CreateOrderRequest
import com.utsman.kemana.remote.merchant.MerchantInstance
import com.utsman.kemana.view.food.OrderStatusActivity
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var cartItems: List<CartItem>
    private var restaurantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        title = "Keranjang Belanja"

        val cartJson = intent.getStringExtra("CART_JSON")
        restaurantId = intent.getStringExtra("RESTAURANT_ID")
        val type = object : TypeToken<List<CartItem>>() {}.type
        cartItems = Gson().fromJson(cartJson, type)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_cart_items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems)

        updateTotal()

        val placeOrderButton: Button = findViewById(R.id.button_place_order)
        placeOrderButton.setOnClickListener {
            placeOrder()
        }
    }

    private fun updateTotal() {
        val totalPrice = cartItems.sumOf { it.menuItem.price * it.quantity }
        val totalTextView: TextView = findViewById(R.id.text_view_total_price)
        totalTextView.text = "Total: Rp $totalPrice"
    }

    private fun placeOrder() {
        if (restaurantId == null) {
            Toast.makeText(this, "ID Restoran tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Ganti dengan lokasi pengguna yang sebenarnya
        val userLat = -6.2087634
        val userLon = 106.845599

        val orderRequest = CreateOrderRequest(
            startLat = userLat,
            startLon = userLon,
            endLat = 0.0, // Lokasi restoran bisa diambil dari API merchant jika tersedia
            endLon = 0.0,
            driverId = restaurantId!!, // Ini seharusnya adalah ID merchant
            items = cartItems.map { Model.Item(it.menuItem.id, it.quantity, it.menuItem.price.toDouble()) }
        )

        lifecycleScope.launch {
            try {
                val response = MerchantInstance.create().createOrder(orderRequest)
                if (response.success == true && response.data != null) {
                    Toast.makeText(this@CartActivity, "Pesanan berhasil dibuat!", Toast.LENGTH_LONG).show()
                    
                    val intent = Intent(this@CartActivity, OrderStatusActivity::class.java)
                    intent.putExtra("jobId", response.data.id)
                    startActivity(intent)
                    
                    finish()
                } else {
                    Toast.makeText(this@CartActivity, "Gagal membuat pesanan: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@CartActivity, "Gagal membuat pesanan: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
