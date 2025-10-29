
package com.ojol.kemana.backend.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

// Enum untuk mendefinisikan tipe pekerjaan
enum class JobType {
    RIDE,
    FOOD,
    MART
}

// Enum untuk status pekerjaan (diperbarui untuk alur merchant)
enum class JobStatus {
    AWAITING_CONFIRMATION, // Menunggu konfirmasi dari merchant
    PREPARING,             // Merchant sedang menyiapkan pesanan
    READY_FOR_PICKUP,      // Pesanan siap dijemput, backend akan mencari driver
    PENDING,               // backend sedang mencari driver (setelah READY_FOR_PICKUP atau untuk RIDE)
    ACCEPTED,              // Driver ditemukan dan menerima
    PICKING_UP,            // Driver sedang menuju lokasi pickup (resto/toko/penumpang)
    IN_PROGRESS,           // Pekerjaan sedang berjalan (mengantar)
    COMPLETED,             // Selesai
    CANCELLED              // Dibatalkan
}

// Interface untuk detail pekerjaan yang spesifik
interface JobDetails

// Detail spesifik untuk RIDE
data class RideDetails(
    val origin: Position,
    val destination: Position,
    val distance: Double // dalam kilometer
) : JobDetails

// Detail spesifik untuk FOOD
data class FoodDetails(
    val merchantId: String,
    val restaurantName: String,
    val restaurantLocation: Position,
    val customerLocation: Position,
    val items: List<OrderItem>
) : JobDetails

// Detail spesifik untuk MART (diperbarui dengan merchantId)
data class MartDetails(
    val merchantId: String,
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
        var status: JobStatus, // Status sekarang menggunakan enum yang baru
        var price: Double, // Ongkos kirim atau tarif perjalanan
        var estimatedTotalPrice: Double? = null, // Total harga barang untuk Mart/Food
        var details: JobDetails, // Polimorfik
        var createdDate: String? = null,
        var updatedDate: String? = null
)
