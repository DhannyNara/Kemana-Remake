
package com.kemana.backend.controller

import com.kemana.backend.* // Mengimpor helper: Ok, JSON_BODY_POST, distanceTo, sendTo
import com.kemana.backend.model.*
import com.kemana.backend.repository.DriverRepository
import com.kemana.backend.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/job")
class JobController {

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var driverRepository: DriverRepository

    // Konstanta untuk jumlah driver terdekat yang akan diberi tahu
    private val TOP_N_DRIVERS = 5

    @PostMapping("/create")
    fun createJob(@RequestBody body: String): Responses {
        val jobTypeString = body.JSON_BODY_POST("jobType")
        val jobType = JobType.valueOf(jobTypeString.toUpperCase())
        val customerId = body.JSON_BODY_POST("customerId")
        val price = body.JSON_BODY_POST("price").toDouble()

        val details = when (jobType) {
            JobType.RIDE -> body.toObject<RideDetails>()
            JobType.FOOD -> body.toObject<FoodDetails>()
            JobType.MART -> body.toObject<MartDetails>()
        }

        val newJob = Job(
                jobType = jobType,
                customerId = customerId,
                price = price,
                details = details,
                status = JobStatus.PENDING,
                createdDate = Instant.now().toString()
        )

        val savedJob = jobRepository.save(newJob)

        // Jalankan logika pencarian driver yang cerdas
        findAndNotifyNearestDrivers(savedJob)

        return Ok(data = savedJob)
    }

    private fun findAndNotifyNearestDrivers(job: Job) {
        // 1. Tentukan titik asal pekerjaan untuk kalkulasi jarak
        val jobOriginPosition = when (val details = job.details) {
            is RideDetails -> details.origin
            is FoodDetails -> details.restaurantLocation
            is MartDetails -> details.storeLocation
        }

        // 2. Dapatkan semua driver yang tersedia dan punya lokasi
        val availableDrivers = driverRepository.findByStatus(DriverStatus.AVAILABLE)
                .filter { it.currentPosition != null }

        if (availableDrivers.isEmpty()) {
            println("No available drivers found.")
            // TODO: Tambahkan logika untuk menangani kasus ini (misal: antrekan pekerjaan)
            return
        }

        // 3. Urutkan driver berdasarkan jarak terdekat ke titik asal pekerjaan
        val sortedDrivers = availableDrivers.sortedBy { driver ->
            driver.currentPosition!!.distanceTo(jobOriginPosition)
        }

        // 4. Ambil N driver teratas
        val nearestDrivers = sortedDrivers.take(TOP_N_DRIVERS)

        // 5. Kirim tawaran pekerjaan hanya ke driver terdekat
        println("Notifying ${nearestDrivers.size} nearest drivers...")
        val message = "{\"type\":\"NEW_JOB_OFFER\", \"jobId\":\"${job.id}\", \"jobDetails\": ${job.details.toJson()}}"

        nearestDrivers.forEach { driver ->
            println("Sending offer to driver ${driver.id}")
            // Menggunakan exchange 'fanout' untuk menyiarkan ke semua instance driver app yang mungkin
            // Namun, karena kita iterasi di backend, kita mengirimkannya satu per satu
            message.sendTo(exchangeName = "job_offers", exchangeType = "fanout")
        }
    }
    
    // TODO: Endpoint untuk driver menerima/menolak pekerjaan
}
