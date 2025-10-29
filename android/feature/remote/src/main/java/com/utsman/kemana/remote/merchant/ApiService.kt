package com.utsman.kemana.remote.merchant

import com.ojol.kemana.KilatOrder
import com.utsman.kemana.remote.driver.Model
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/v1/merchants")
    suspend fun getMerchants(@Query("type") type: String): Model.Responses

    @GET("/api/v1/merchants/{id}/menu")
    suspend fun getMenu(@Path("id") id: String): List<com.ojol.kemana.MenuItem> // Perbaikan: Mengembalikan List

    @POST("/api/v1/job/create")
    suspend fun createOrder(@Body request: CreateOrderRequest): Model.Responses

    @GET("/api/v1/job/{jobId}")
    suspend fun getOrderStatus(@Path("jobId") jobId: String): Model.Responses

    @POST("/api/v1/order/kilat/create")
    suspend fun createKilatOrder(@Body order: KilatOrder): Model.Responses
}
