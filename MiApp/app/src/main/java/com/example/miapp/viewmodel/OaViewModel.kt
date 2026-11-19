package com.example.miapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miapp.data.AppDatabase
import com.example.miapp.data.OaEntity
import com.example.miapp.data.ProgresoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OaViewModel(application: Application) : AndroidViewModel(application) {
    private val oaDao = AppDatabase.getDatabase(application).oaDao()

    // Flujos en tiempo real según perfil
    val listaOas: Flow<List<OaEntity>> = oaDao.obtenerTodosLosOas()
    val listaOasPublicados: Flow<List<OaEntity>> = oaDao.obtenerOasPublicados()
    val listaProgreso: Flow<List<ProgresoEntity>> = oaDao.obtenerTodoElProgreso()

    private fun obtenerFechaHoraActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    fun guardarOa(oa: OaEntity) {
        viewModelScope.launch {
            oaDao.insertarOa(oa)
        }
    }

    fun eliminarOa(oa: OaEntity) {
        viewModelScope.launch {
            oaDao.eliminarOa(oa)
        }
    }

    // Flujo de creación con validación de reglas de negocio
    fun validarYGuardarOa(
        codigo: String,
        titulo: String,
        intentosStr: String,
        fecha: String,
        requiereAudio: Boolean,
        publicadoInmediato: Boolean,
        asignatura: String,
        descripcion: String,
        autoria: String = "Contenido Propio",
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
        if (intentos == null || intentos <= 0 || intentos > 5) {
            onError("Los intentos permitidos deben estar entre 1 y 5.")
            return
        }
        if (fecha.isBlank()) {
            onError("La fecha de disponibilidad es obligatoria.")
            return
        }

        val estadoInicial = if (publicadoInmediato) "Publicado" else "Pendiente UTP"
        val trazaInicial = if (publicadoInmediato) {
            "Aprobado y publicado por UTP (${obtenerFechaHoraActual()})"
        } else {
            "Registrado - Pendiente autorización UTP (${obtenerFechaHoraActual()})"
        }

        val nuevoOa = OaEntity(
            codigo = codigo.trim().uppercase(),
            titulo = titulo.trim(),
            asignatura = asignatura,
            unidad = "Unidad 1",
            descripcion = descripcion.trim(),
            tipoRecurso = if (requiereAudio) "Audio / Texto" else "Texto",
            estado = estadoInicial,
            autoria = autoria,
            fechaModificacion = fecha,
            intentosMaximos = intentos,
            trazabilidadUtp = trazaInicial
        )

        viewModelScope.launch {
            oaDao.insertarOa(nuevoOa)
            onExito(nuevoOa)
        }
    }

    // --- ACCIONES EXCLUSIVAS DE JEFATURA UTP ---
    fun aprobarYPublicarOaUtp(oa: OaEntity) {
        viewModelScope.launch {
            val fechaActual = obtenerFechaHoraActual()
            val traza = "Autorizado y publicado por Jefatura UTP el $fechaActual"
            oaDao.cambiarEstadoUtp(
                oaId = oa.id,
                nuevoEstado = "Publicado",
                trazabilidad = traza,
                fecha = fechaActual
            )
        }
    }

    fun deshabilitarOaUtp(oa: OaEntity) {
        viewModelScope.launch {
            val fechaActual = obtenerFechaHoraActual()
            val traza = "Deshabilitado por Jefatura UTP el $fechaActual"
            oaDao.cambiarEstadoUtp(
                oaId = oa.id,
                nuevoEstado = "Deshabilitado",
                trazabilidad = traza,
                fecha = fechaActual
            )
        }
    }

    // --- ACCIONES DEL ESTUDIANTE (PRUEBA FINAL Y CONTROL DE INTENTOS) ---
    fun registrarResultadoPrueba(
        oa: OaEntity,
        puntajeObtenido: Int,
        puntajeTotal: Int,
        detalleRespuestas: String,
        onResultadoGuardado: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val intentosPrevios = oaDao.contarIntentosRealizados(oa.codigo)
            if (intentosPrevios >= oa.intentosMaximos) {
                onResultadoGuardado(false, "Has alcanzado el límite de ${oa.intentosMaximos} intentos para este OA.")
                return@launch
            }

            val intentoActual = intentosPrevios + 1
            val porcentaje = if (puntajeTotal > 0) (puntajeObtenido * 100) / puntajeTotal else 0

            val nuevoProgreso = ProgresoEntity(
                estudianteFicticio = "Estudiante 2°B - Ficticio 01",
                codigoOa = oa.codigo,
                tituloOa = oa.titulo,
                asignatura = oa.asignatura,
                intentoNumero = intentoActual,
                puntajeObtenido = puntajeObtenido,
                puntajeTotal = puntajeTotal,
                porcentajeAvance = porcentaje,
                detalleRespuestas = detalleRespuestas,
                marcaTiempo = obtenerFechaHoraActual()
            )

            oaDao.insertarProgreso(nuevoProgreso)
            val restantes = oa.intentosMaximos - intentoActual
            onResultadoGuardado(
                true,
                "¡Excelente! Obtuviste $puntajeObtenido/$puntajeTotal ($porcentaje%). Intentos restantes: $restantes."
            )
        }
    }

    // --- ACCIONES EXCLUSIVAS DEL DOCENTE (INFORMES PEDAGÓGICOS) ---
    fun registrarObservacionDocente(progresoId: Int, observacion: String) {
        if (observacion.isBlank()) return
        viewModelScope.launch {
            oaDao.guardarObservacionDocente(progresoId, observacion.trim())
        }
    }
}