package com.jsoro.notesapp.feature_note.domain.use_case

import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository

// Esta clase es idéntica a GetNotes, con la diferencia de que no devuelve nada
// y por tanto no filtra ningún resultado.
class DeleteNote (
    private val repository: NoteRepository
){
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}