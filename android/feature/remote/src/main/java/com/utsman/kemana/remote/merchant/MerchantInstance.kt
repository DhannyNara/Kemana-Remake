package com.utsman.kemana.remote.merchant

import com.utsman.kemana.remote.driver.RemoteInstance
import retrofit2.Retrofit

object MerchantInstance {
    fun create(): ApiService {
        val retrofit = RemoteInstance.retrofit()
        return retrofit.create(ApiService::class.java)
    }
}
