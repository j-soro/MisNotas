package com.jsoro.notesapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoro.notesapp.feature_note.domain.model.InvalidNoteException
import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Esta clase es un ViewModel secundario respecto de nuestro ViewModel principal (NotesViewModel).
// Se asocia con la vista de editar/añadir una nota y contiene el estado de los campos que serán
// modificados al editar o crear notas.
// La clase será inyectada como dependencia en la aplicación de la misma forma que NotesViewModel.

// Tiene como valores-parámetros:
// - Casos de uso de las notas, para guardarlas o crearlas (noteUseCases).
// - Un handle del estado guardado de la aplicación, para poder pasarle una nota concreta al caso
// de uso durante la inicialización del ViewModel. Sirve para cargar una nota, ya que guardaremos
// el Id de la misma en el estado guardado de navegación.

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Hay estados separados para que no se recomponga la interfaz de usuario al completo a no ser
    // que realmente sea necesario. Por ejemplo si cambia el título de la nota no debería volver a
    // generarse toda la vista de edición.

    // Este campo observa el estado de nuestra clase NoteTextFieldState, que es un envoltorio para
    // guardar el estado del título de la nota. De forma que tendrá un valor por defecto y si se
    // introduce texto en el valor se guardará en este ViewModel.
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Escribe un título..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    // Este campo es igual que el anterior pero para el cuerpo de la nota.
    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Escribe contenido..."
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    // Este campo es similar a los anteriores, guarda el valor de color de la nota, que por defecto
    // será aleatorio.
    private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    // Este campo es una forma de tener controlados eventos que no forman parte del estado. Existen
    // valores que son parte del estado, como los campos anteriores, y que deben cambiar mediante
    // eventos cuando en la interfaz se realicen determinadas acciones. Sin embargo hay otro tipo de
    // elementos que no son tan importantes o estructurales como los que hemos definido arriba.
    // Los elementos que deben guardar un estado son de tipo State, como los anteriores, mientras
    // los que no son tan importantes serán un "flujo de eventos", como por ejemplo mostrar una
    // notificación o un snackbar.
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // El id de la nota abierta
    private var currentNoteId: Int? = null

    // Inicialización del ViewModel: Se toma el valor "noteId" guardado savedStateHandle, y si no es
    // null, dependiendo de si el Id vale -1 (valor por defecto), se lanza una corutina para obtener
    // la nota asociada a ese Id (use case GetNote) y se cargan los datos de la nota en el estado
    // de este ViewModel.
    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch{
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    // Función que recibe como parámetro un evento lanzado por la interfaz de usuario y realiza una
    // acción dependiendo del tipo de evento recibido. Comportamiento igual al de NotesViewModel.
    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            // Si hemos añadido texto al título
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    // Actualizamos el valor guardado para el título
                    text = event.value
                )
            }
            // Si hemos cambiado el título
            is AddEditNoteEvent.ChangedTitleFocus -> {
                // Actualizamos el valor guardado para el título
                _noteTitle.value = noteTitle.value.copy(
                    // Ocultamos la pista "Introduce el título..." si está en blanco y escribimos
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            // Si hemos añadido texto al contenido de la nota
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    // Actualizamos el valor guardado para el título
                    text = event.value
                )
            }
            // Si hemos cambiado el contenido del cuerpo de la nota
            is AddEditNoteEvent.ChangedContentFocus -> {
                // Actualizamos el valor guardado para el título
                _noteContent.value = noteContent.value.copy(
                    // Ocultamos la pista "Introduce el título..." si está en blanco y escribimos
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            // Si cambia el color seleccionado
            is AddEditNoteEvent.ChangeColor -> {
                // Tomamos el seleccionado que acompaña al evento lanzado
                _noteColor.value = event.color
            }
            // Si se ha clicado el botón de guardar nota
            is AddEditNoteEvent.SaveNote -> {
                // Se lanza corutina
                viewModelScope.launch {
                    // Intentamos guardar la nota
                    try{
                        // Si no hay problemas, la añadimos mediante el caso de uso correspondiente
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        // Y emitimos el evento a la interfaz de usuario
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        // Si hay algún problema, que no debería, mostramos mensaje de error
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Error al guardar la nota"
                            )
                        )
                    }
                }
            }
        }
    }

    // Clase sellada que contiene un snackbar, el cual mostraremos en el evento superior, en el caso
    // de pulsar el botón de guardar nota. También contiene el evento que se lanzará en caso de que
    // la nota sea guardada (en este caso, cambiaremos de vista en el interfaz de usuario).
    sealed class UiEvent {
        data class ShowSnackbar(val message: String):UiEvent()
        object SaveNote: UiEvent()
    }
}