package com.jsoro.notesapp.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsoro.notesapp.feature_note.domain.model.Note

// Esta clase es un RoomDatabase, indicado por la anotación @Database, y contiene objetos NoteDao
// que son entidades mediante las cuales accedemos a los datos de una nota.

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}