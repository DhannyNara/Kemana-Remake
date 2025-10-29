
package com.ojol.kemana.backend.controller

import com.ojol.kemana.backend.model.Driver
import com.ojol.kemana.backend.model.LoginRequest
import com.ojol.kemana.backend.model.Responses
import com.ojol.kemana.backend.repository.DriverDbRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/db/driver")
class DriverDbController {

    @Autowired
    lateinit var driverDbRepository: DriverDbRepository

    @PostMapping("/login")
    fun loginDriver(@RequestBody loginRequest: LoginRequest): ResponseEntity<Responses> {
        val driver = driverDbRepository.findById(loginRequest.id)

        return if (driver.isPresent) {
            val foundDriver = driver.get()
            // PENTING: Di aplikasi nyata, bandingkan password yang sudah di-hash
            if (foundDriver.password == loginRequest.password) {
                val successResponse = Responses("OK", listOf(foundDriver))
                ResponseEntity.ok(successResponse)
            } else {
                val errorResponse = Responses("FAILED", "Password salah")
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
            }
        } else {
            val errorResponse = Responses("FAILED", "User ID tidak ditemukan")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        }
    }

    // --- Endpoint yang sudah ada sebelumnya ---

    @PostMapping("/")
    fun saveDriver(@RequestBody driver: Driver): Responses {
        driverDbRepository.save(driver)
        return Responses("OK", listOf(driver))
    }

    @GetMapping("/check/{email}")
    fun checkRegisteredDriver(@PathVariable("email") email: String): Responses {
        val drivers = driverDbRepository.findAll().filter { it.email == email }
        return Responses("OK", !drivers.isNullOrEmpty())
    }

    @GetMapping("/registered")
    fun getRegisteredDriver(): Responses {
        val drivers = driverDbRepository.findAll()
        return Responses("OK", drivers)
    }

    // ... (Endpoint lainnya tetap ada di sini)
}
