package com.example.miapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Asignatura(
    val id: String,
    val nombre: String,
    val iconoResId: Int? = null
)

@Entity(tableName = "objetivos_aprendizaje")
data class ObjetivoAprendizaje(
    @PrimaryKey val codigo: String,
    val titulo: String,
    val descripcionEscrita: String,
    val audioUrlOrRes: String? = null,
    val estado: String = "Publicado"
)

data class PruebaFinal(
    val id: String,
    val oaCodigo: String,
    val intentosPermitidos: Int = 2,
    val intentosUsados: Int = 0
)

data class Pregunta(
    val id: Int,
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int
)