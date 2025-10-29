
package com.ojol.kemana.backend

import com.ojol.kemana.backend.model.Position // Import data class Position
import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.StandardCharsets

// --- Fungsi Kalkulasi Jarak ---

/**
 * Menghitung jarak antara dua titik koordinat geografis menggunakan formula Haversine.
 * @param other Titik Position lain yang akan dihitung jaraknya.
 * @return Jarak dalam meter.
 */
fun Position.distanceTo(other: Position): Double {
    val earthRadiusKm = 6371.0

    val dLat = Math.toRadians(other.latitude - this.latitude)
    val dLon = Math.toRadians(other.longitude - this.longitude)

    val originLatRad = Math.toRadians(this.latitude)
    val destLatRad = Math.toRadians(other.latitude)

    val a = Math.pow(Math.sin(dLat / 2), 2.0) + Math.pow(Math.sin(dLon / 2), 2.0) * Math.cos(originLatRad) * Math.cos(destLatRad)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return earthRadiusKm * c * 1000 // Hasil dalam meter
}


// --- Manajemen RabbitMQ ---

object RabbitMQManager {
    private var connectionFactory: ConnectionFactory? = null

    fun getConnectionFactory(): ConnectionFactory {
        if (connectionFactory == null) {
            connectionFactory = ConnectionFactory()
            connectionFactory!!.host = "localhost"
        }
        return connectionFactory!!
    }
}

/**
 * Ekstensi pada String untuk mengirim pesan ke RabbitMQ.
 */
fun String.sendTo(exchangeName: String, exchangeType: String, routingKey: String = "") {
    try {
        val factory = RabbitMQManager.getConnectionFactory()
        factory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.exchangeDeclare(exchangeName, exchangeType, true)
                channel.basicPublish(exchangeName, routingKey, null, this.toByteArray(StandardCharsets.UTF_8))
                println(" [x] Sent to exchange '$exchangeName' -> '$this'")
            }
        }
    } catch (e: Exception) {
        println("Error sending message to RabbitMQ: ${e.message}")
        e.printStackTrace()
    }
}
