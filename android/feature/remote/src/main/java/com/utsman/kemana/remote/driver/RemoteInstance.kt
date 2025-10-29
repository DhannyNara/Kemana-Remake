
/*
 * Copyright (c) 2019 Muhammad Utsman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.kemana.remote.driver

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Definisikan ApiService interface di sini
interface ApiService {
    @POST("/api/db/driver/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}

object RemoteInstance {

    private const val BASE_URL = "http://192.168.1.10:8080/"

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Buat public property untuk ApiService
    val api: ApiService by lazy {
        retrofit().create(ApiService::class.java)
    }
}