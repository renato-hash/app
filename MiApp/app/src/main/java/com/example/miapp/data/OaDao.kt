package com.example.miapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miapp.model.ObjetivoAprendizaje
import kotlinx.coroutines.flow.Flow

@Dao
interface OaDao {
    @Query("SELECT * FROM objetivos_aprendizaje")
    fun obtenerTodos(): Flow<List<ObjetivoAprendizaje>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(oa: ObjetivoAprendizaje)
}