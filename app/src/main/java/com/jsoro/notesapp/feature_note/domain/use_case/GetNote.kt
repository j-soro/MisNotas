package com.jsoro.notesapp.feature_note.domain.use_case

import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository

// Esta clase representa un caso de uso: obtener una única nota de un repositorio.
// No importa el origen exacto de los datos, ya que para eso creamos un repositorio
// GetNote puede ser invocado como función y recibe un repositorio como parámetro
// Devuelve una única nota, que se obtendrá cuando la seleccionemos en la vista principal de la
// aplicación

// Esta clase se utiliza principalmente con el ViewModel secundario (AddEditViewModel), para poder
// abrir una nota existente y editar su contenido.

class GetNote (
    private val repository : NoteRepository

) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}