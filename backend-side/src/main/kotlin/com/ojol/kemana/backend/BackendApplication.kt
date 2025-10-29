
package com.ojol.kemana.backend

import com.ojol.kemana.backend.model.Driver
import com.ojol.kemana.backend.repository.DriverDbRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class BackendApplication {

    // Bean ini akan dijalankan saat aplikasi pertama kali start
    @Bean
    fun init(driverDbRepository: DriverDbRepository) = CommandLineRunner {
        // Cek apakah driver demo sudah ada
        val demoDriverId = "000001"
        val existingDriver = driverDbRepository.findById(demoDriverId)

        if (existingDriver.isEmpty) {
            println("Membuat akun driver demo...")
            val demoDriver = Driver(
                    id = demoDriverId,
                    name = "Demo Driver",
                    email = "demo@kemana.com",
                    password = "demo", // Di aplikasi nyata, ini HARUS di-hash!
                    photoUrl = null,
                    vehicleType = "MOTOR",
                    vehiclePlate = "B 1234 DEM"
            )
            driverDbRepository.save(demoDriver)
            println("Akun driver demo berhasil dibuat dengan ID: $demoDriverId")
        } else {
            println("Akun driver demo sudah ada.")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
