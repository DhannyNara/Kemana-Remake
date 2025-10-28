
package com.kemana.backend.repository

import com.kemana.backend.model.Driver
import com.kemana.backend.model.DriverStatus
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverRepository : MongoRepository<Driver, String> {

    // Menemukan driver berdasarkan ID
    fun findDriverById(id: String): Driver?

    // Menemukan driver berdasarkan email
    fun findDriverByEmail(email: String): Driver?

    // Query kustom untuk menemukan semua driver dengan status tertentu
    fun findByStatus(status: DriverStatus): List<Driver>

}
