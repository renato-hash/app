package com.example.miapp.data.remote

import com.example.miapp.model.ProgresoRemote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProgresoApiService {

    @GET("progreso")
    suspend fun listarProgresos(): List<ProgresoRemote>

    @POST("progreso")
    suspend fun crearProgreso(@Body progreso: ProgresoRemote): ProgresoRemote

    @PUT("progreso/{id}")
    suspend fun actualizarProgreso(
        @Path("id") id: Long,
        @Body progreso: ProgresoRemote
    ): ProgresoRemote

    @DELETE("progreso/{id}")
    suspend fun eliminarProgreso(@Path("id") id: Long): Response<Unit>
}


