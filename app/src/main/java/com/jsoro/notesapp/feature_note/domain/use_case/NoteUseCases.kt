package com.jsoro.notesapp.feature_note.domain.use_case

// Esta clase será inyectada en nuestro ViewModel, de forma que obtengamos exactamente
// las instancias de los casos de uso que necesita nuestra aplicación
data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote
)
