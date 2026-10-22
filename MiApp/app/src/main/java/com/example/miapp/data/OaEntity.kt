package com.example.miapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objetivos_aprendizaje")
data class OaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val titulo: String,
    val asignatura: String,
    val unidad: String,
    val descripcion: String,
    val tipoRecurso: String = "Texto",
    val estado: String = "Borrador",
    val autoria: String = "Propio",
    val fechaModificacion: String = ""
)