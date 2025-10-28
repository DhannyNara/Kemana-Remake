
package com.kemana.backend.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

// Enum untuk status driver
enum class DriverStatus {
    OFFLINE,    // Tidak aktif atau tidak bekerja
    AVAILABLE,  // Online dan siap menerima pekerjaan
    ON_JOB      // Sedang mengerjakan sebuah pekerjaan
}

@Document(collection = "drivers") // Menggunakan satu koleksi "drivers"
data class Driver(
        @Id
        var id: String? = null,
        val name: String,
        val email: String, // Sebaiknya unik
        val photoUrl: String?,

        // Properti yang akan sering diupdate
        var currentPosition: Position? = null, // Menggunakan Position dari model/Position.kt
        var status: DriverStatus = DriverStatus.OFFLINE,
        
        // Atribut yang jarang berubah
        val vehicleType: String, // Misal: "MOTOR", "MOBIL"
        val vehiclePlate: String,
        
        // Informasi sensitif (sebaiknya dienkripsi)
        var balance: Double = 0.0
)
