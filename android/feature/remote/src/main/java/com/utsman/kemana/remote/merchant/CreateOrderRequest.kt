package com.utsman.kemana.remote.merchant

import com.google.gson.annotations.SerializedName

// Menggunakan kembali model Item yang sudah ada jika sesuai,
// atau definisikan yang baru jika strukturnya berbeda.
// Untuk saat ini, kita asumsikan Model.Item dari modul driver bisa dipakai.
import com.utsman.kemana.remote.driver.Model

data class CreateOrderRequest(
    @SerializedName("start_lat")
    val startLat: Double,
    @SerializedName("start_lon")
    val startLon: Double,
    @SerializedName("end_lat")
    val endLat: Double,
    @SerializedName("end_lon")
    val endLon: Double,
    @SerializedName("driver_id")
    val driverId: String,
    @SerializedName("order_type")
    val orderType: String = "FOOD",
    @SerializedName("payment_method")
    val paymentMethod: String = "CASH",
    @SerializedName("items")
    val items: List<Model.Item>
)
