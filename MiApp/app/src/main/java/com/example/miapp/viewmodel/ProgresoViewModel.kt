package com.example.miapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.model.ProgresoRemote
import com.example.miapp.repository.ProgresoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgresoViewModel(
    private val repository: ProgresoRepository = ProgresoRepository()
) : ViewModel() {

    private val _progresosList = MutableStateFlow<List<ProgresoRemote>>(emptyList())
    val progresosList: StateFlow<List<ProgresoRemote>> = _progresosList

    private val _mensajeEstado = MutableStateFlow<String?>(null)
    val mensajeEstado: StateFlow<String?> = _mensajeEstado

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    init {
        cargarProgresos()
    }

    fun cargarProgresos() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                _progresosList.value = repository.obtenerProgresos()
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al cargar desde servidor: ${e.localizedMessage}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun guardarProgreso(progreso: ProgresoRemote) {
        viewModelScope.launch {
            try {
                repository.guardarProgreso(progreso)
                _mensajeEstado.value = "Progreso sincronizado con MySQL"
                cargarProgresos()
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al guardar en servidor: ${e.localizedMessage}"
            }
        }
    }

    fun actualizarProgreso(id: Long, progresoActualizado: ProgresoRemote) {
        viewModelScope.launch {
            try {
                repository.actualizarProgreso(id, progresoActualizado)
                _mensajeEstado.value = "Validación actualizada en servidor"
                cargarProgresos()
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al actualizar: ${e.localizedMessage}"
            }
        }
    }

    fun eliminarProgreso(id: Long) {
        viewModelScope.launch {
            try {
                if (repository.eliminarProgreso(id)) {
                    _mensajeEstado.value = "Registro eliminado del servidor"
                    cargarProgresos()
                }
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al eliminar: ${e.localizedMessage}"
            }
        }
    }

    fun limpiarMensaje() {
        _mensajeEstado.value = null
    }
}

