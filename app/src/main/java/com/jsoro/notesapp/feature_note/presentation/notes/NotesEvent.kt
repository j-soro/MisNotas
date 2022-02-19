package com.jsoro.notesapp.feature_note.presentation.notes

import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.util.NoteOrder

// Esta clase contiene los eventos que el usuario puede realizar en la interfaz de usuario. Estos
// lanzarán un evento al ViewModel para que cambie el estado de la app (NotesState) en consecuencia.
// Es una clase sellada de Kotlin, que mandará los eventos de nuestros @Composables a nuestro
// ViewModel.

// Cuando se sigue la arquitectura Clean, que es la que se está practicando con este proyecto, es
// importante pensar en las acciones que pueden realizarse desde la interfaz de usuario, para poder
// diseñar eventos que disparen esas acciones internamente y proporcionen los resultados esperados.

// Estas acciones/eventos son:
// - Cambiar la ordenación de las notas.
// - Borrar una nota.
// - Mostrar/ocultar las opciones de ordenación.
// - Restaurar una nota después de haberla borrado (deshacer).

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note):NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
