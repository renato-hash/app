package com.example.miapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OaDao {
    @Query("SELECT * FROM objetivos_aprendizaje")
    fun obtenerTodosLosOas(): Flow<List<OaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOa(oa: OaEntity)
}