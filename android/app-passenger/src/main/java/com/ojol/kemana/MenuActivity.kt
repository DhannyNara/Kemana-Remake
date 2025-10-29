package com.ojol.kemana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.utsman.kemana.remote.merchant.MerchantInstance
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    private val cart = mutableMapOf<MenuItem, Int>()
    private lateinit var viewCartButton: Button
    private var restaurantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        restaurantId = intent.getStringExtra("RESTAURANT_ID")
        val restaurantName = intent.getStringExtra("RESTAURANT_NAME")
        title = restaurantName ?: "Pilih Menu"

        if (restaurantId == null) {
            Toast.makeText(this, "ID Restoran tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewCartButton = findViewById(R.id.button_view_cart)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_menu_items)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val menuItems = MerchantInstance.create().getMenu(restaurantId!!)
                val adapter = MenuAdapter(menuItems) { menuItem -> // Perbaikan: Langsung menggunakan menuItems
                    val currentQuantity = cart.getOrPut(menuItem) { 0 }
                    cart[menuItem] = currentQuantity + 1
                    updateCartButton()
                    Toast.makeText(this@MenuActivity, "${menuItem.name} ditambahkan", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MenuActivity, "Gagal memuat menu: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewCartButton.setOnClickListener {
            if (cart.isEmpty()) {
                Toast.makeText(this, "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cartItems = cart.map { CartItem(it.key, it.value) }
            val cartJson = Gson().toJson(cartItems)

            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("CART_JSON", cartJson)
                putExtra("RESTAURANT_ID", restaurantId)
            }
            startActivity(intent)
        }

        updateCartButton()
    }

    private fun updateCartButton() {
        val totalItems = cart.values.sum()
        viewCartButton.text = "Lihat Keranjang ($totalItems)"
    }
}
