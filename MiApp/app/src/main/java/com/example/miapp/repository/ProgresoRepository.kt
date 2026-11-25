package com.example.miapp.repository

import com.example.miapp.data.remote.ProgresoApiService
import com.example.miapp.data.remote.ProgresoRetrofitInstance
import com.example.miapp.model.ProgresoRemote

class ProgresoRepository(
    private val api: ProgresoApiService = ProgresoRetrofitInstance.api
) {
    suspend fun obtenerProgresos(): List<ProgresoRemote> {
        return api.listarProgresos()
    }

    suspend fun guardarProgreso(progreso: ProgresoRemote): ProgresoRemote {
        return api.crearProgreso(progreso)
    }

    suspend fun actualizarProgreso(id: Long, progreso: ProgresoRemote): ProgresoRemote {
        return api.actualizarProgreso(id, progreso)
    }

    suspend fun eliminarProgreso(id: Long): Boolean {
        val respuesta = api.eliminarProgreso(id)
        return respuesta.isSuccessful
    }
}

