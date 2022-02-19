package com.jsoro.notesapp.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsoro.notesapp.feature_note.presentation.notes.components.NoteItem
import com.jsoro.notesapp.feature_note.presentation.notes.components.OrderSection
import kotlinx.coroutines.launch

// Esta clase es el @Composable principal de la aplicación. Contiene los composables que se pueden
// encontrar en el paquete "components" dentro de la estructura de la aplicación. Dentro tenemos la
// sección de filtros, ocultable mediante un botón, así como el título principal de la aplicación y
// cada nota existente en la base datos de la app.

// Mencionar que la etiqueta @ExperimentalAnimationApi se debe al uso de animaciones para mostrar y
// ocultar la sección de filtros, que requiere del uso de esta etiqueta.

// Tiene como valores-parámetros:
// - Controlador de navegación (navController).
// - ViewModel de la aplicación, obtenido como dependencia inyectada (viewModel).

// Tiene como variables:
// - El estado de la aplicación, obtenido desde el ViewModel (estate)
// - Un Scaffold, que es un objeto de Compose que nos permite construir estructuras fácilmente,
// una base para cualquier layout de una aplicación en Compose.
// - El scope, que es una referencia al ámbito de ejecución de la app, para poder lanzar una corutina
// (coroutine). Es necesario para utilizar el snackbar emergente que permite deshacer el borrado de
// una nota.
@ExperimentalAnimationApi
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel:NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        // Botón flotante para añadir nueva nota.
        floatingActionButton = {
            FloatingActionButton(onClick = {
            }, backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir nota")
            }
        },
        // Estado actual del layout.
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis Notas",
                    style = MaterialTheme.typography.h4
                )
                // Botón para mostrar y ocultar los filtros.
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Ordenar"
                    )
                }
            }
            // Cuando cambie isOrderSectionVisible, se animará el mostrar/ocultar de la sección
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                // Sección de filtros que será ocultada y mostrada.
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    // Evento que indica que hemos cambiado el tipo de ordenamiento al ViewModel.
                    onOrderChanged = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Columna que contiene las distintas notas (NoteItem)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Para cada nota en el estado (NotesState), crea un NoteItem
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            // Ejecutar cuando se seleccione una nota en concreto mediante clic.
                            .clickable {
                                /* TODO */
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            // Debe lanzarse una co-rutina (coroutine) para el mensaje emergente.
                            scope.launch {
                                // El resultado de la acción emergente, si es pulsada o no.
                               val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Nota eliminada",
                                    actionLabel = "Deshacer acción"
                                )
                                // Si hemos clicado en la acción sugerida en el snackbar (deshacer).
                                if(result == SnackbarResult.ActionPerformed) {
                                    // Llamamos al evento de restaurar nota en el ViewModel.
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    // Espacio entre notas
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
