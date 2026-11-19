package com.example.miapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progreso_estudiantes")
data class ProgresoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val estudianteFicticio: String = "Estudiante 2°B - N°01",
    val codigoOa: String,
    val tituloOa: String,
    val asignatura: String,
    val intentoNumero: Int,
    val puntajeObtenido: Int,
    val puntajeTotal: Int,
    val porcentajeAvance: Int,
    val detalleRespuestas: String,
    val marcaTiempo: String,
    val observacionDocente: String = "Sin observaciones registradas",
    val validadoPorDocente: Boolean = false
)
