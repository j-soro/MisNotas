package com.jsoro.notesapp.feature_note.domain.use_case

import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository
import com.jsoro.notesapp.feature_note.domain.util.NoteOrder
import com.jsoro.notesapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Esta clase representa un caso de uso: obtener notas (flujo de datos) de un repositorio
// No importa el origen exacto de los datos, ya que para eso creamos un repositorio
// GetNotes puede ser invocado como función y recibe un NoteOrder como parámetro
// Devuelve un flujo de datos (Flow<List<Note>>) filtrado a partir de un flujo obtenido
// en el repositorio
class GetNotes (
    private val repository: NoteRepository
) {
    // Necesitamos pasar un parámetro para que quien llame a esta función pueda
    // indicar la manera en la que quiere recibir (filtrar) el flujo de datos.

    // Para ello creamos dos clases en util: OrderType y NoteOrder
    // 'operator' se utiliza para poder invocar a la clase como una función
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map {

            // Aplicamos al flujo de datos las funciones sortedBy o sortedByDescending
            // dependiendo del tipo de NoteOrder recibido como parámetro en el invoke()
            // de esta clase, esto se hace mediante la función map
            // (que recibe una lambda: 'notes -> {}' como parámetro)
                notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }

                }
            }
        }

    }
}