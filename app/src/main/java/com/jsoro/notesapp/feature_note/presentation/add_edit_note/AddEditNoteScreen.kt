package com.jsoro.notesapp.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Esta clase es un @Composable para el diseño de la pantalla de añadir/editar nota. Es el segundo
// componente principal de nuestra capa de vista o presentación, junto con NotesScreen.kt que es la
// pantalla principal.

// La idea es seleccionar una nota en la pantalla principal (o crear una nueva) y pasar a esta
// usando un NavController. Pasamos como parámetro el viewModel que guardará el estado de la app en
// esa pantalla de añadir/editar, además también pasamos un int para que al cargar una nota ésta no
// tenga un color aleatorio (lo cual ocurre al crear una nueva nota, como hemos establecido en el
// ViewModel (AddEditNoteViewModel). Si la nota seleccionada tiene asignado un color, debe mostrarse
// en la pantalla con ese color, y sin pasarlo como parámetro, recibiría uno aleatorio.
// Además existe una animación para el cambio de color, empleando el @Composable Animatable.

// Tiene como valores-parámetros:
// - El controlador de navegación, el cual le pasa como argumento la Id de la nota (-1 si es nueva)
// (navController).
// - El color de la nota seleccionada en caso de no estar creando una nueva nota (noteColor).
// - El ViewModel para la pantalla de añadir/editar notas, es decir, la instancia existente en hilt,
// como dependencia inyectada (viewModel).

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()

    // Animación de cambio de color
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if(noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    val scope = rememberCoroutineScope()

    // Este @Composable ejecuta un bloque de código al ser compuesto en ejecución. Cuando ese bloque
    // se ejecuta, toma los últimos eventos presentes en el ViewModel y, si encuentra los eventos de
    // mostrar el snackbar o de guardar una nota, ejecuta una acción correspondiente.
    // - Si hay evento de mostrar snackbar -> lo muestra con el mensaje del evento.
    // - Si hay evento de guardar nota -> vuelve a la pantalla anterior.
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            // Si se encuentra un evento
            when(event) {
                // Si es un evento para mostrar el snackbar.
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                // Si es un evento para guardar la nota.
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }
    // Estructura principal de la composición de la pantalla.
    Scaffold (
        // Botón flotante para guardar los cambios realizados en la nota o la nota creada.
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Guardar nota")
            }
        },
        // Estado actual del layout
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            // Fila con la selección de colores para cambiar el color de fondo de la nota.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Generar por cada posible color un @Composable Box con ese color.
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(18.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                // Si el color es el seleccionado, borde negro para distinguir
                                color = if (viewModel.noteColor.value == colorInt) {
                                    Color.Black
                                // Si no es el seleccionado, borde transparente.
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            // Al clicar los botones de cada color, lanzamos en el ámbito actual
                            // de ejecución una animación para que cambie el color del fondo.
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 600
                                        )
                                    )
                                }
                                // Tras cambiar el color, notificamos mediante un evento al estado.
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Pista: "Introduce un título..."
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                // Si cambia el valor del título, lanzamos evento de que se ha introducido texto.
                onValueChanged = {
                                 viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                // Si cambia el foco al título o lo pierde, lanzamos evento de que ha cambiado.
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangedTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Pista: "Introduce el contenido de la nota..."
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                // Si cambia el valor del título, lanzamos evento de que se ha introducido texto.
                onValueChanged = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                // Si cambia el foco al título o lo pierde, lanzamos evento de que ha cambiado.
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangedContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}