package com.example.miapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExternalRetrofitInstance {
    val api: ExternalApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://date.nager.at/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExternalApiService::class.java)
    }
}