package com.ojol.kemana.merchant

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ojol.kemana.merchant.remote.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderListActivity : AppCompatActivity() {

    private lateinit var orderAdapter: OrderAdapter
    private val orders = mutableListOf<Order>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        title = "Daftar Pesanan"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://kemana.herokuapp.com/") // Ganti dengan URL base API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_orders)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(orders, {
            // Aksi terima pesanan
            acceptOrder(it)
        }, {
            // Aksi tolak pesanan
            rejectOrder(it)
        })
        recyclerView.adapter = orderAdapter

        startPolling()
    }

    private fun acceptOrder(order: Order) {
        lifecycleScope.launch {
            try {
                val response = apiService.acceptOrder(order.id)
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderListActivity, "Pesanan #${order.id.substring(0, 5)} diterima", Toast.LENGTH_SHORT).show()
                    fetchOrders() // Muat ulang daftar pesanan
                } else {
                    Toast.makeText(this@OrderListActivity, "Gagal menerima pesanan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@OrderListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rejectOrder(order: Order) {
        lifecycleScope.launch {
            try {
                val response = apiService.rejectOrder(order.id)
                if (response.isSuccessful) {
                    Toast.makeText(this@OrderListActivity, "Pesanan #${order.id.substring(0, 5)} ditolak", Toast.LENGTH_SHORT).show()
                    fetchOrders() // Muat ulang daftar pesanan
                } else {
                    Toast.makeText(this@OrderListActivity, "Gagal menolak pesanan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@OrderListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPolling() {
        lifecycleScope.launch {
            while (isActive) {
                fetchOrders()
                delay(10000) // Polling setiap 10 detik
            }
        }
    }

    private fun fetchOrders() {
        lifecycleScope.launch {
            try {
                val newOrders = apiService.getOrders("PENDING")
                orders.clear()
                orders.addAll(newOrders)
                orderAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
                // Tidak menampilkan toast error di sini agar tidak mengganggu pengguna saat polling
            }
        }
    }
}
