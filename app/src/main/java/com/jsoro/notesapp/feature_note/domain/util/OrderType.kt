package com.jsoro.notesapp.feature_note.domain.util

// Esta clase contiene los dos tipos de ordenamiento posibles para el filtrado
// de un flujo de datos (ascendente, descendente), y funciona en conjunto con
// NoteOrder, dentro de GetNotes (en use_case), nuestro caso de uso principal
sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
