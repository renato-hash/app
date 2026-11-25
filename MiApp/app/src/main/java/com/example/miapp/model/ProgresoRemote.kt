package com.example.miapp.model

import com.example.miapp.data.ProgresoEntity

data class ProgresoRemote(
    val id: Long? = null,
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

fun ProgresoEntity.toRemote(): ProgresoRemote {
    return ProgresoRemote(
        id = if (this.id == 0) null else this.id.toLong(),
        estudianteFicticio = this.estudianteFicticio,
        codigoOa = this.codigoOa,
        tituloOa = this.tituloOa,
        asignatura = this.asignatura,
        intentoNumero = this.intentoNumero,
        puntajeObtenido = this.puntajeObtenido,
        puntajeTotal = this.puntajeTotal,
        porcentajeAvance = this.porcentajeAvance,
        detalleRespuestas = this.detalleRespuestas,
        marcaTiempo = this.marcaTiempo,
        observacionDocente = this.observacionDocente,
        validadoPorDocente = this.validadoPorDocente
    )
}