package com.jsoro.notesapp.feature_note.data.repository

import com.jsoro.notesapp.feature_note.data.data_source.NoteDao
import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

// Implementación de la interfaz de dominio
// Se trata de tomar las funciones de la interfaz NoteRepository
// Para trabajar con datos reales utilizamos una instancia de nuestro DAO
// el cual accede a la base de datos y gestiona entidades (Note.kt)

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}