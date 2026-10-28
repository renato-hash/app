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

    fun validarYGuardarOa(
        codigo: String,
        titulo: String,
        intentosStr: String,
        fecha: String,
        requiereAudio: Boolean,
        publicadoInmediato: Boolean,
        asignatura: String,
        descripcion: String,
        onExito: (OaEntity) -> Unit,
        onError: (String) -> Unit
    ) {

        if (codigo.isBlank()) {
            onError("El código OA no puede estar vacío.")
            return
        }
        if (titulo.isBlank()) {
            onError("El título no puede estar vacío.")
            return
        }
        val intentos = intentosStr.toIntOrNull()
        if (intentos == null || intentos <= 0) {
            onError("Los intentos deben ser un número entero válido (ej: 3).")
            return
        }
        if (fecha.isBlank()) {
            onError("La fecha de disponibilidad es obligatoria.")
            return
        }

        val nuevoOa = OaEntity(
            codigo = codigo,
            titulo = titulo,
            asignatura = asignatura,
            unidad = "Unidad 1",
            descripcion = descripcion,
            tipoRecurso = if (requiereAudio) "Audio / Texto" else "Texto",
            estado = if (publicadoInmediato) "Publicado" else "Borrador",
            autoria = "Contenido Propio",
            fechaModificacion = fecha
        )

        viewModelScope.launch {
            oaDao.insertarOa(nuevoOa)
            onExito(nuevoOa)
        }
    }
}