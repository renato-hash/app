package com.example.miapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oas")
data class OaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo: String,
    val titulo: String,
    val asignatura: String,
    val unidad: String = "Unidad 1",
    val descripcion: String,
    val tipoRecurso: String = "Texto",
    val estado: String = "Pendiente UTP",
    val autoria: String = "Contenido Propio",
    val fechaModificacion: String,
    val intentosMaximos: Int = 3,
    val trazabilidadUtp: String = "Creado - En espera de validación UTP"
)