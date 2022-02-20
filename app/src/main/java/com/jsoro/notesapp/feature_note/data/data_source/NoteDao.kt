package com.jsoro.notesapp.feature_note.data.data_source

import androidx.room.*
import com.jsoro.notesapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

// Esta clase es un DAO (Data Access Object), parte del framework Room para realizar bases de datos
// en Android. Tiene funciones que devuelven objetos Note o bien flujos de objetos Note, según
// ciertas queries realizadas a la base de datos SQLite, y que se designan mediante las anotaciones
// inferiores según el tipo de operación CRUD.

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}