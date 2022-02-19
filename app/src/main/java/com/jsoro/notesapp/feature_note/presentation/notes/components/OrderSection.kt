package com.jsoro.notesapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsoro.notesapp.feature_note.domain.util.NoteOrder
import com.jsoro.notesapp.feature_note.domain.util.OrderType

// Esta clase es un clase fichero @Composable para el diseño de la sección de ordenado/filtrado.
// Contiene otros @Composable que son copias de nuestro DefaultRadioButton.

// Tiene como valores-parámetros:
// - Modifier por defecto de Compose (modifier).
// - Ordenación de la notas NoteOrder, por defecto por fecha descendiente (noteOrder).
// - Función lambda que se ejecuta al cambiar ese ordenamiento (onOrderChanged).
@Composable
fun OrderSection(
    modifier : Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChanged: (NoteOrder) -> Unit
    ) {
    Column(
        modifier = modifier
    ) {
        DefaultRadioButton(
            text = "Título",
            selected = noteOrder is NoteOrder.Title,
            onSelect = { onOrderChanged(NoteOrder.Title(noteOrder.orderType)) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        DefaultRadioButton(
            text = "Fecha",
            selected = noteOrder is NoteOrder.Date,
            onSelect = { onOrderChanged(NoteOrder.Date(noteOrder.orderType)) }
        )
        Spacer(modifier = Modifier.width(10.dp))
        DefaultRadioButton(
            text = "Color",
            selected = noteOrder is NoteOrder.Color,
            onSelect = { onOrderChanged(NoteOrder.Color(noteOrder.orderType)) }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        DefaultRadioButton(
            text = "Ascendiente",
            selected = noteOrder.orderType is OrderType.Ascending,
            onSelect = {
                onOrderChanged(noteOrder.copy(OrderType.Ascending))
            }
        )
        Spacer(modifier = Modifier.width(10.dp))
        DefaultRadioButton(
            text = "Descendiente",
            selected = noteOrder.orderType is OrderType.Descending,
            onSelect = {
                onOrderChanged(noteOrder.copy(OrderType.Descending))
            }
        )

    }
}