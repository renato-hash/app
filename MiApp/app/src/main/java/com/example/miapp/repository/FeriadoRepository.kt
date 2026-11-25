package com.example.miapp.repository

import com.example.miapp.data.remote.ExternalRetrofitInstance
import com.example.miapp.model.FeriadoChile

class FeriadoRepository {
    suspend fun getFeriados(): List<FeriadoChile> {
        return ExternalRetrofitInstance.api.getFeriadosChile()
    }
}
