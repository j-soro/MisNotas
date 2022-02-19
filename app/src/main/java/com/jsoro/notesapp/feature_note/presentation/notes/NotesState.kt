package com.jsoro.notesapp.feature_note.presentation.notes

import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.util.NoteOrder
import com.jsoro.notesapp.feature_note.domain.util.OrderType

// Esta clase, como se comenta en NotesViewModel, es un envoltorio o wrapper del estado de nuestra
// aplicación, concretamente de lo que concierne a la vista o interfaz de usuario. Es una clase de
// datos Kotlin.

// Necesitamos tener en este NotesState:
// - La ordenación actual de las notas. Por defecto será por fecha y descendente.
// - Lista de notas de la base de datos. Por defecto estará vacía.
// - Indicador de si los filtros están visibles o no. Por defecto estarán ocultos.

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
)



