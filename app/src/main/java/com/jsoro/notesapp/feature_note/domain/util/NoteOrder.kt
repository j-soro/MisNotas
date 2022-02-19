package com.jsoro.notesapp.feature_note.domain.util

// Esta clase contiene las tres formas de ordenar un flujo de datos (de notas).
// Junto con OrderType, serán usadas para filtrar las notas existentes.
sealed class NoteOrder (val orderType: OrderType) {

    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Color(orderType: OrderType): NoteOrder(orderType)

    // Esta función sirve para que, al tener las notas ordenadas por un campo concreto (por título,
    // fecha o color), podamos cambiar el ordenamiento (ascendente o descendente) sin perder ese
    // orden por campo con ello.
    fun copy(orderType: OrderType): NoteOrder {
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}
