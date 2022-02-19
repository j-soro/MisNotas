package com.jsoro.notesapp.feature_note.domain.use_case

import com.jsoro.notesapp.feature_note.domain.model.InvalidNoteException
import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository

// Esta clase representa un caso de uso: crear una nueva nota dentro de un repositorio.
// Sólo insertaremos una nota nueva en caso de que hayamos validado previamente el título y texto.
// Si hay un problema con la validación, no la insertaremos.
// AddNote puede ser invocado como función.
// Devuelve un flujo de datos (Flow<List<Note>>) filtrado a partir de un flujo obtenido
// en el repositorio

class AddNote (
    private val repository: NoteRepository
){
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("El título de la nota no puede estar vacío.")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("La nota no tiene contenido.")
        }
        repository.insertNote(note)

    }
}