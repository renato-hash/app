package com.example.miapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OaDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOa(oa: OaEntity)

    @Update
    suspend fun actualizarOa(oa: OaEntity)

    @Delete
    suspend fun eliminarOa(oa: OaEntity)

    // UTP ve todos los OAs (pendientes, publicados y deshabilitados)
    @Query("SELECT * FROM oas ORDER BY id DESC")
    fun obtenerTodosLosOas(): Flow<List<OaEntity>>

    // Docente y Estudiante solo ven los OAs autorizados y publicados por UTP
    @Query("SELECT * FROM oas WHERE estado = 'Publicado' ORDER BY id DESC")
    fun obtenerOasPublicados(): Flow<List<OaEntity>>

    // Acción exclusiva UTP: Aprobar/Publicar o Deshabilitar con trazabilidad
    @Query("UPDATE oas SET estado = :nuevoEstado, trazabilidadUtp = :trazabilidad, fechaModificacion = :fecha WHERE id = :oaId")
    suspend fun cambiarEstadoUtp(oaId: Int, nuevoEstado: String, trazabilidad: String, fecha: String)

    // --- GESTIÓN DE PROGRESO E INFORMES PEDAGÓGICOS ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProgreso(progreso: ProgresoEntity)

    @Query("SELECT * FROM progreso_estudiantes ORDER BY id DESC")
    fun obtenerTodoElProgreso(): Flow<List<ProgresoEntity>>

    @Query("SELECT COUNT(*) FROM progreso_estudiantes WHERE codigoOa = :codigoOa")
    suspend fun contarIntentosRealizados(codigoOa: String): Int

    // Acción exclusiva Docente: Guardar observación pedagógica y validar informe
    @Query("UPDATE progreso_estudiantes SET observacionDocente = :observacion, validadoPorDocente = 1 WHERE id = :progresoId")
    suspend fun guardarObservacionDocente(progresoId: Int, observacion: String)
}