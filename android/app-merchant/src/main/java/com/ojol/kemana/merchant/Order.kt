package com.ojol.kemana.merchant

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: String,
    @SerializedName("items")
    val items: List<OrderItem>,
    @SerializedName("total")
    val total: Double,
    @SerializedName("status")
    val status: String
)

data class OrderItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: Double
)
