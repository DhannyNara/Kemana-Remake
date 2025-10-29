package com.ojol.kemana.backend.controller

import com.ojol.kemana.backend.distanceTo
import com.ojol.kemana.backend.model.Position
import com.ojol.kemana.backend.model.Responses
import com.ojol.kemana.backend.model.Driver
import com.ojol.kemana.backend.repository.DriverRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/driver")
class DriverController {
    @Autowired
    lateinit var driverRepository: DriverRepository

    @RequestMapping(value = ["/active"], method = [RequestMethod.GET])
    fun getActiveDriver(): Responses {
        val drivers = driverRepository.findAll()
        return Responses("OK", drivers)
    }

    @RequestMapping(value = ["/active/range"], method = [RequestMethod.GET])
    fun getActiveDriverByRange(
            @RequestParam("from") from: String,
            @RequestParam("distance") distance: Double
    ): Responses {
        val allDrivers = driverRepository.findAll()
        val listCoordinate = from.split(",")
        val lat = listCoordinate[0].toDouble()
        val lon = listCoordinate[1].toDouble()

        val mPosition = Position(lat, lon, 0.0)

        val result = allDrivers.filter { mPosition.distanceTo(it.position!!) <= distance }

        return Responses("OK", result)
    }

    @RequestMapping(value = ["/active/email"], method = [RequestMethod.GET])
    fun getActiveEmailDriver(): Responses {
        val drivers = driverRepository.findAll()
        val emails = drivers.map { it.email }
        return Responses("OK", emails)
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun saveDriver(@Valid @RequestBody driver: Driver): Responses {
        driver.id = UUID.nameUUIDFromBytes(driver.email.toByteArray()).toString()
        driverRepository.save(driver)
        return Responses("OK", listOf(driver))
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getDriver(@PathVariable("id") id: String): Responses {
        val driver = driverRepository.findDriverById(id)
        return Responses("OK", listOf(driver))
    }

    @RequestMapping(value = [""], method = [RequestMethod.GET])
    fun getDriverByEmail(@RequestParam("email") email: String): Responses {
        val driver = driverRepository.findDriverByEmail(email)
        return Responses("OK", listOf(driver))
    }

    @RequestMapping(value = [""], method = [RequestMethod.PUT])
    fun editPositionByEmail(@RequestParam("email") email: String, @Valid @RequestBody position: Position): Responses {
        val driver = driverRepository.findDriverByEmail(email)
        driver?.position = position
        driverRepository.save(driver!!)
        return Responses("OK", listOf(driver))
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun editPosition(@PathVariable("id") id: String, @Valid @RequestBody position: Position): Responses {
        val driver = driverRepository.findDriverById(id)
        driver?.position = position
        driverRepository.save(driver!!)
        return Responses("OK", listOf(driver))
    }

    @RequestMapping(value = [""], method = [RequestMethod.DELETE])
    fun deleteByEmail(@RequestParam("email") email: String): Responses {
        val driver = driverRepository.findDriverByEmail(email)

        return if (driver != null) {
            driver.let { driverRepository.delete(it) }
            Responses("OK", listOf(null))
        } else {
            Responses("FAILED", listOf(null))
        }
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun deleteDriver(@PathVariable("id") id: String): Responses {
        val driver = driverRepository.findDriverById(id)

        return if (driver != null) {
            driver.let { driverRepository.delete(it) }
            Responses("OK", listOf(null))
        } else {
            Responses("FAILED", listOf(null))
        }
    }
}