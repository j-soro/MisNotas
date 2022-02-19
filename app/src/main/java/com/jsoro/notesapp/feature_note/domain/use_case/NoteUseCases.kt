package com.jsoro.notesapp.feature_note.domain.use_case

// Esta clase será inyectada en nuestro ViewModel, de forma que obtengamos exactamente
// las instancias de los casos de uso que necesita nuestra aplicación. Esta forma de trabajar
// es propia de la arquitectura Clean, en la cual tenemos inyectadas las dependencias de nuestra
// aplicación de forma que la ejecución es controlada y no hay fugas de memoria o entidades zombie.
data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote
)
