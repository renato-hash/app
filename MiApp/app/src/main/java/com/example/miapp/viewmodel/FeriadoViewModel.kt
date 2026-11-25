package com.example.miapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.model.FeriadoChile
import com.example.miapp.repository.FeriadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeriadoViewModel : ViewModel() {
    private val repository = FeriadoRepository()
    private val _feriadosList = MutableStateFlow<List<FeriadoChile>>(emptyList())
    val feriadosList: StateFlow<List<FeriadoChile>> = _feriadosList

    init {
        fetchFeriados()
    }

    private fun fetchFeriados() {
        viewModelScope.launch {
            try {
                _feriadosList.value = repository.getFeriados()
            } catch (e: Exception) {
                println("Error al obtener feriados: ${e.localizedMessage}")
            }
        }
    }
}

