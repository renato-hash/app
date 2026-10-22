package com.example.miapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.data.AppDatabase
import com.example.miapp.data.OaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OaViewModel(application: Application) : AndroidViewModel(application) {
    private val oaDao = AppDatabase.getDatabase(application).oaDao()
    val listaOas: Flow<List<OaEntity>> = oaDao.obtenerTodosLosOas()

    fun guardarOa(oa: OaEntity) {
        viewModelScope.launch {
            oaDao.insertarOa(oa)
        }
    }
}