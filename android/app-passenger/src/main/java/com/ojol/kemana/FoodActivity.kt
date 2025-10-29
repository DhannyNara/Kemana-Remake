package com.ojol.kemana

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utsman.kemana.remote.driver.Model
import com.utsman.kemana.remote.merchant.MerchantInstance
import kotlinx.coroutines.launch

class FoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        title = "Pilih Restoran"

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_restaurants)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val response = MerchantInstance.create().getMerchants("FOOD")
                val restaurants = response.data?.map { driver ->
                    Restaurant(driver.id ?: "", driver.name ?: "", "Food Stall")
                } ?: emptyList()

                val adapter = RestaurantAdapter(restaurants) { restaurant ->
                    val intent = Intent(this@FoodActivity, MenuActivity::class.java).apply {
                        putExtra("RESTAURANT_ID", restaurant.id)
                        putExtra("RESTAURANT_NAME", restaurant.name)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@FoodActivity, "Gagal memuat restoran: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
