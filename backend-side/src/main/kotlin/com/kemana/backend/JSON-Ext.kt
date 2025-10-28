
package com.kemana.backend

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

// Inisialisasi Gson, library untuk konversi JSON
private val gson = Gson()

/**
 * Ekstensi untuk mengekstrak nilai dari body request JSON secara aman.
 * @param key Kunci dari field JSON yang ingin diambil.
 * @return Nilai dari field sebagai String, atau string kosong jika tidak ditemukan.
 */
fun String.JSON_BODY_POST(key: String): String {
    return try {
        val jsonObject = JsonParser.parseString(this).asJsonObject
        jsonObject.get(key)?.asString ?: ""
    } catch (e: Exception) {
        ""
    }
}

/**
 * Ekstensi generik untuk mengkonversi String JSON menjadi objek Kotlin.
 * Menggunakan reified type parameter untuk menangani berbagai tipe data secara dinamis.
 * Contoh penggunaan: "{\"latitude\":1.0, \"longitude\":2.0}".toObject<Position>()
 * @return Objek Kotlin dari tipe yang ditentukan.
 */
inline fun <reified T> String.toObject(): T {
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this, type)
}

/**
 * Ekstensi untuk mengkonversi objek Kotlin (Any) menjadi representasi String JSON.
 * @return String JSON dari objek.
 */
fun Any.toJson(): String {
    return gson.toJson(this)
}

