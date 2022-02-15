package com.jsoro.notesapp.feature_note.domain.util

// Esta clase contiene las tres formas de ordenar un flujo de datos (de notas)
// Junto con OrderType, serán usadas para filtrar las notas existentes
sealed class NoteOrder (val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Color(orderType: OrderType): NoteOrder(orderType)

}
