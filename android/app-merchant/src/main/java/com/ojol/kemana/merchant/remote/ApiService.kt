package com.ojol.kemana.merchant.remote

import com.ojol.kemana.merchant.Order
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/jobs/food")
    suspend fun getOrders(
        @Query("status") status: String // e.g., "PENDING", "ACCEPTED"
    ): List<Order>

    @POST("api/v1/jobs/food/{orderId}/accept")
    suspend fun acceptOrder(@Path("orderId") orderId: String): Response<Void>

    @POST("api/v1/jobs/food/{orderId}/reject")
    suspend fun rejectOrder(@Path("orderId") orderId: String): Response<Void>
}
