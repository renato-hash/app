package com.example.miapp.repository

import com.example.miapp.data.NoteDao
import com.example.miapp.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao: NoteDao) {
    val notes: Flow<List<Note>> = dao.observeAll()

    suspend fun save(title: String, content: String) {
        dao.insert(Note(title = title.trim(), content = content.trim()))
    }

    suspend fun delete(note: Note) {
        dao.delete(note)
    }
}