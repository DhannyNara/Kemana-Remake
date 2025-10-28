
package com.kemana.backend.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

// Enum untuk mendefinisikan tipe pekerjaan
enum class JobType {
    RIDE, // Ojek atau taksi
    FOOD, // Pesan antar makanan
    MART, // Belanja kebutuhan sehari-hari
    KILAT, // Pengiriman barang instan
    JASTIP // Jasa titip
}

// Enum untuk status pekerjaan
enum class JobStatus {
    PENDING,        // Mencari driver
    ACCEPTED,       // Driver ditemukan dan menerima
    PICKING_UP,     // Driver sedang menuju lokasi pickup (resto/toko/penumpang)
    IN_PROGRESS,    // Pekerjaan sedang berjalan (mengantar penumpang/barang)
    COMPLETED,      // Selesai
    CANCELLED       // Dibatalkan
}

// Interface untuk detail pekerjaan yang spesifik
// Ini memungkinkan kita memiliki detail yang berbeda untuk setiap JobType
interface JobDetails

// Detail spesifik untuk RIDE
data class RideDetails(
    val origin: Position,
    val destination: Position,
    val distance: Double // dalam kilometer
) : JobDetails

// Detail spesifik untuk FOOD
data class FoodDetails(
    val restaurantId: String,
    val restaurantName: String,
    val restaurantLocation: Position,
    val customerLocation: Position,
    val items: List<OrderItem>
) : JobDetails

// Detail spesifik untuk MART
data class MartDetails(
    val storeName: String,
    val storeLocation: Position,
    val customerLocation: Position,
    val items: List<OrderItem> // Daftar belanjaan
) : JobDetails

// Model untuk item dalam pesanan (Food/Mart)
data class OrderItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val price: Double,
    val notes: String?
)

@Document(collection = "jobs")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Job(
        @Id
        var id: String? = null,
        var jobType: JobType,
        var customerId: String,
        var driverId: String? = null,
        var status: JobStatus = JobStatus.PENDING,
        var price: Double, // Ongkos kirim atau tarif perjalanan
        var estimatedTotalPrice: Double? = null, // Total harga barang untuk Mart/Food
        var details: JobDetails, // Polimorfik, akan berisi RideDetails, FoodDetails, dll.
        var createdDate: String? = null,
        var updatedDate: String? = null
)