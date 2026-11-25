package com.example.miapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProgresoRetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:9093/"

    val api: ProgresoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProgresoApiService::class.java)
    }
}

