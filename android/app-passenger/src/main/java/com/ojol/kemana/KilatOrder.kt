
package com.ojol.kemana

import com.google.gson.annotations.SerializedName

data class KilatOrder(
    @SerializedName("pickup_address")
    val pickupAddress: Address,
    @SerializedName("destination_address")
    val destinationAddress: Address,
    @SerializedName("vehicle")
    val vehicle: String,
    @SerializedName("package_type")
    val packageType: String,
    @SerializedName("courier_note")
    val courierNote: String? = null,
    @SerializedName("use_motorbox")
    val useMotorbox: Boolean = false,
    @SerializedName("send_sms")
    val sendSms: Boolean = false,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("promo_code")
    val promoCode: String? = null
)

data class Address(
    @SerializedName("address")
    val address: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("pickup_time")
    val pickupTime: String,
    @SerializedName("note")
    val note: String? = null
)
