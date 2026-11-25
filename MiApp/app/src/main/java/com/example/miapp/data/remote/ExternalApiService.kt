package com.example.miapp.data.remote

import com.example.miapp.model.FeriadoChile
import retrofit2.http.GET

interface ExternalApiService {
    @GET("api/v3/PublicHolidays/2026/CL")
    suspend fun getFeriadosChile(): List<FeriadoChile>
}