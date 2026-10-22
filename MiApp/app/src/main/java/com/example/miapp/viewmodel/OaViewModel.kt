package com.example.miapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.data.AppDatabase
import com.example.miapp.model.ObjetivoAprendizaje
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OaViewModel(private val db: AppDatabase) : ViewModel() {

    // Lista de OAs consultada desde la Base de Datos Room
    val listaOa: StateFlow<List<ObjetivoAprendizaje>> = db.oaDao().obtenerTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Campos del Formulario de UTP
    var codigoInput by mutableStateOf("")
    var tituloInput by mutableStateOf("")
    var descripcionInput by mutableStateOf("")
    var horasInput by mutableStateOf("") // Campo tipo Int
    var fechaInput by mutableStateOf("2026-10-21") // Campo tipo Date
    var activoInput by mutableStateOf(true) // Campo Switch
    var audioRequeridoInput by mutableStateOf(true) // Campo Checkbox

    // Mensajes de Error para Validaciones Visuales
    var codigoError by mutableStateOf<String?>(null)
    var tituloError by mutableStateOf<String?>(null)
    var horasError by mutableStateOf<String?>(null)

    fun validarYGuardar(): Boolean {
        var esValido = true

        if (codigoInput.isBlank()) {
            codigoError = "El código es obligatorio (ej. OA 01)"
            esValido = false
        } else {
            codigoError = null
        }

        if (tituloInput.isBlank()) {
            tituloError = "El título no puede estar vacío"
            esValido = false
        } else {
            tituloError = null
        }

        val horasInt = horasInput.toIntOrNull()
        if (horasInt == null || horasInt <= 0) {
            horasError = "Ingrese horas válidas (número entero positivo)"
            esValido = false
        } else {
            horasError = null
        }

        if (esValido) {
            viewModelScope.launch {
                val nuevoOa = ObjetivoAprendizaje(
                    codigo = codigoInput.trim(),
                    titulo = tituloInput.trim(),
                    descripcionEscrita = "$descripcionInput | Horas: $horasInput | Fecha: $fechaInput"
                )
                db.oaDao().insertar(nuevoOa)
                limpiarFormulario()
            }
        }
        return esValido
    }

    private fun limpiarFormulario() {
        codigoInput = ""
        tituloInput = ""
        descripcionInput = ""
        horasInput = ""
        codigoError = null
        tituloError = null
        horasError = null
    }
}