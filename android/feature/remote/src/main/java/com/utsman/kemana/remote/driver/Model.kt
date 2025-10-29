
/*
 * Copyright (c) 2019 Muhammad Utsman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.kemana.remote.driver

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.utsman.kemana.remote.place.Places
import kotlinx.android.parcel.Parcelize
import java.util.*

// Login-specific data classes
data class LoginRequest(
    @SerializedName("id")
    val id: String,
    @SerializedName("password")
    val password: String
)

// Menggunakan kembali Driver model dari backend
@Parcelize
data class Driver(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String, 
    @SerializedName("password")
    val password: String? = null, // Sebaiknya tidak diekspos ke UI
    @SerializedName("photoUrl")
    val photoUrl: String?,
    @SerializedName("currentPosition")
    var currentPosition: Position? = null,
    @SerializedName("status")
    var status: String? = "OFFLINE", // Sesuaikan dengan enum di backend
    @SerializedName("vehicleType")
    val vehicleType: String, 
    @SerializedName("vehiclePlate")
    val vehiclePlate: String,
    @SerializedName("balance")
    var balance: Double = 0.0
) : Parcelable


@Parcelize
data class Position(
    var lat: Double? = 0.0,
    var lon: Double? = 0.0,
    var angle: Double? = 0.0
) : Parcelable

@Parcelize
data class Attribute(
    var vehiclesType: String? = "passenger",
    var vehiclesPlat: String? = "passenger"
) : Parcelable

// Generic Response untuk Login
data class LoginResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("payload")
    val payload: List<Driver>?,
    @SerializedName("message")
    val message: String? // Tambahkan message untuk error handling
)


// Model-model lain yang sudah ada
@Parcelize
data class Passenger(
    var id: String? = null,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    var position: Position? = null
) : Parcelable

data class Responses(
    val message: String,
    val data: List<Driver>? = null
)

data class ResponsesAttribute(
    val message: String,
    val attrs: List<Attribute>? = null
)

data class ResponsesEmail(
    val message: String,
    val data: List<String>? = null
)

data class ResponsesChecking(
    val message: String,
    val data: Boolean?
)

data class OrderData(
    @SerializedName("id")
    val orderID: String?,
    val accepted: Boolean,
    val from: Places?,
    val to: Places?,
    val attribute: OrderDataAttr
)

data class OrderDataAttr(
    val driver: Driver?,
    val passenger: Passenger?
)

data class OrderResponses(
    val message: String,
    val data: OrderData
)

object RemoteState {
    const val INSERT_DRIVER = 10
    const val ALL_DRIVER = 11
    const val DRIVER = 12
    const val EDIT_DRIVER = 13
    const val DELETE_DRIVER = 14
    const val STOP_EDIT_DRIVER = 15
}
