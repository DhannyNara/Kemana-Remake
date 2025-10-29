
package com.ojol.kemana.backend.controller

import com.ojol.kemana.backend.* // Mengimpor helper
import com.ojol.kemana.backend.model.*
import com.ojol.kemana.backend.repository.DriverRepository
import com.ojol.kemana.backend.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/v1/job")
class JobController {

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var driverRepository: DriverRepository

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

        // LOGIKA BARU: Tentukan status awal berdasarkan tipe pekerjaan
        val initialStatus = when (jobType) {
            JobType.FOOD, JobType.MART -> JobStatus.AWAITING_CONFIRMATION
            JobType.RIDE -> JobStatus.PENDING
        }

        val newJob = Job(
                jobType = jobType,
                customerId = customerId,
                price = price,
                details = details,
                status = initialStatus, // Menggunakan status dinamis
                createdDate = Instant.now().toString()
        )

        val savedJob = jobRepository.save(newJob)

        // Arahkan pekerjaan berdasarkan tipenya
        when (savedJob.jobType) {
            JobType.RIDE -> {
                println("Ride job created. Finding nearest drivers...")
                findAndNotifyNearestDrivers(savedJob)
            }
            JobType.FOOD, JobType.MART -> {
                println("${savedJob.jobType} job created. Notifying merchant with status AWAITING_CONFIRMATION...")
                notifyRelevantMerchant(savedJob)
            }
        }

        return Ok(data = savedJob)
    }

    @PostMapping("/merchant/update")
    fun handleMerchantUpdate(@RequestBody body: String): ResponseEntity<Responses> {
        val jobId = body.JSON_BODY_POST("jobId")
        val newStatusString = body.JSON_BODY_POST("newStatus")
        val newStatus = JobStatus.valueOf(newStatusString.toUpperCase())

        val job = jobRepository.findById(jobId).orElse(null)
                ?: return ResponseEntity.status(404).body(Responses("failed", "Job not found", null))

        job.status = newStatus
        val updatedJob = jobRepository.save(job)
        println("Job ${job.id} status updated to $newStatus by merchant.")

        if (newStatus == JobStatus.READY_FOR_PICKUP) {
            println("Job ${job.id} is ready for pickup. Finding nearest drivers...")
            findAndNotifyNearestDrivers(updatedJob)
        }

        return ResponseEntity.ok(Ok(data = updatedJob))
    }

    private fun notifyRelevantMerchant(job: Job) {
        val merchantId = when (val details = job.details) {
            is FoodDetails -> details.merchantId
            is MartDetails -> details.merchantId
            else -> null
        }

        if (merchantId == null) {
            println("Error: Merchant ID not found for job ${job.id}")
            return
        }
        
        val message = "{\"type\":\"NEW_ORDER\", \"jobId\":\"${job.id}\", \"jobDetails\": ${job.details.toJson()}}"
        
        println("Sending new order notification to merchant: $merchantId for job: ${job.id}")
        message.sendTo(exchangeName = "merchant_notifications", exchangeType = "direct", routingKey = merchantId)
    }

    private fun findAndNotifyNearestDrivers(job: Job) {
        val jobOriginPosition = when (val details = job.details) {
            is RideDetails -> details.origin
            is FoodDetails -> details.restaurantLocation
            is MartDetails -> details.storeLocation
        }

        val availableDrivers = driverRepository.findByStatus(DriverStatus.AVAILABLE)
                .filter { it.currentPosition != null }

        if (availableDrivers.isEmpty()) {
            println("No available drivers found for job ${job.id}.")
            return
        }

        val sortedDrivers = availableDrivers.sortedBy { driver ->
            driver.currentPosition!!.distanceTo(jobOriginPosition)
        }

        val nearestDrivers = sortedDrivers.take(TOP_N_DRIVERS)

        println("Notifying ${nearestDrivers.size} nearest drivers for job ${job.id}...")
        val message = "{\"type\":\"NEW_JOB_OFFER\", \"jobId\":\"${job.id}\", \"jobDetails\": ${job.details.toJson()}}"

        message.sendTo(exchangeName = "job_offers", exchangeType = "fanout", routingKey = "")
    }
}
