package com.example.miapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.data.AppDatabase
import com.example.miapp.model.ObjetivoAprendizaje
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OaViewModel(private val db: AppDatabase) : ViewModel() {

    val listaOa: StateFlow<List<ObjetivoAprendizaje>> = db.oaDao().obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun guardarOa(codigo: String, titulo: String, descripcion: String) {
        if (codigo.isNotBlank() && titulo.isNotBlank()) {
            viewModelScope.launch {
                val nuevoOa = ObjetivoAprendizaje(codigo, titulo, descripcion)
                db.oaDao().insertar(nuevoOa)
            }
        }
    }
}