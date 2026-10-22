package com.example.miapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miapp.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Note>>
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(note: Note)
    @Delete
    suspend fun delete(note: Note)
}