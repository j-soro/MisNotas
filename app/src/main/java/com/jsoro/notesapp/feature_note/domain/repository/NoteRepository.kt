package com.jsoro.notesapp.feature_note.domain.repository

import com.jsoro.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

// Una función por cada una presente en nuestro DAO
// Esta clase es la interfaz que debe implementar nuestra capa de acceso a datos
// Por ese motivo las funciones sólo estan declaradas y no tienen cuerpo.
interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)



}