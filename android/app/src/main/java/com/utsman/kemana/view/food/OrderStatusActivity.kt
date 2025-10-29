package com.utsman.kemana.view.food

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utsman.kemana.R
import com.utsman.kemana.remote.merchant.MerchantInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class OrderStatusActivity : AppCompatActivity() {

    private var jobId: String? = null
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        jobId = intent.getStringExtra("jobId")
        statusTextView = findViewById(R.id.text_view_status)

        if (jobId == null) {
            Toast.makeText(this, "Error: ID Pesanan tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        startPolling()
    }

    private fun startPolling() {
        lifecycleScope.launch {
            while (isActive) {
                fetchOrderStatus()
                delay(5000) // Poll every 5 seconds
            }
        }
    }

    private fun fetchOrderStatus() {
        lifecycleScope.launch {
            try {
                val response = MerchantInstance.create().getOrderStatus(jobId!!)
                if (response.success == true && response.data != null) {
                    updateStatus(response.data.status!!)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // Do not show toast to avoid spamming the user
            }
        }
    }

    private fun updateStatus(status: String) {
        val statusText = when (status) {
            "PENDING" -> "Menunggu konfirmasi merchant..."
            "ACCEPTED" -> "Pesanan diterima!"
            "REJECTED" -> "Pesanan ditolak."
            "DRIVER_FOUND" -> "Driver ditemukan!"
            // TODO: Add other statuses
            else -> "Status tidak diketahui: $status"
        }
        statusTextView.text = statusText
    }
}
