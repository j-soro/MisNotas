package com.jsoro.notesapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsoro.notesapp.feature_note.domain.model.Note
import com.jsoro.notesapp.feature_note.domain.use_case.NoteUseCases
import com.jsoro.notesapp.feature_note.domain.util.NoteOrder
import com.jsoro.notesapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// Esta clase es el ViewModel de nuestra aplicación. Es un archivo/fichero Kotlin, por lo que
// contiene funciones sino datos o campos.

// Un ViewModel es una clase que está directamente acoplada a la capa de vista o presentación
// El ejemplo típico de arquitectura en la que se usa esto es MVVM (lo que he utilizado para el
// proyecto final de la asignatura de interfaces). Existe un Modelo, una Vista, y un ViewModel que
// funciona como intermediario.

// Sin embargo en este caso el ViewModel es ligeramente distinto, no tendrá relación con la lógica
// de negocio, en su lugar lo que hará es utilizar los casos de uso (use cases), que son las clases
// que contienen la lógica de negocio.

// El ViewModel tomará los resultados de las funciones que hemos
// creado en las clases de los Use Cases, de forma que la interfaz de usuario pueda observar al
// ViewModel y tomar de él esos resultados.

// No se trata de contener datos o variables sin más, sino que utilizaremos la clase NotesState como
// un envoltorio o wrapper de dicha información. Este funcionará como "estado" de la aplicación.
// Necesitamos tener en ese NotesState:
// - La ordenación actual de las notas.
// - Lista de notas de la base de datos.
// - Indicador de si los filtros están visibles o no.

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    // Este campo contiene los valores que definen el estado de nuestra aplicación, utilizando la
    // clase NotesState.
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    // Este campo contiene una nota que se obtiene al borrar una nota en la interfaz, para poder
    // deshacer el borrado, como puede verse unas lineas más abajo.
    private var recentlyDeletedNote: Note? = null

    // Este campo contiene un resultado de búsqueda, relacionado con la obtención de notas según un
    // ordenamiento concreto. La idea es evitar que cada vez que cambie el ordenamiento se vuelvan
    // a cargar las notas en un flujo nuevo sin borrar el previo, así que tendremos esta constante
    // para almacenar un único Job de getNotes.
    private var getNotesJob: Job? = null

    // Inicialización del flujo de datos para obtener notas aunque no hayaos seleccionado ningún
    // ordenamiento ni recibido otros eventos desde la interfaz de usuario.
    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    // Función que lanzaremos desde la interfaz de usuario para lanzar el evento correspondiente
    // No todos los eventos se relacionan con los casos de uso. En la mayoría de ellos sencillamente
    // cambiaremos el valor de un campo dentro de NotesState para reflejar la acción realizada.
    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {
                // Si el orden actual en el estado es el mismo al que cambiamos
                // (los ::class son para evitar comparar referencias, usamos entidades)
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        // Y el ordenamiento también es el mismo al que cambiamos
                        state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    // No cambiamos nada
                    return
                }
                getNotes(event.noteOrder)


            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    // Podemos ejecutar esta clase como función gracias al modificador "operator"
                    // Se envía un Note con el evento para este evento
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }

            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    // En caso de que la nota sea Null, volvemos a la ejecución principal de la app.
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    // Para no reinsertar si volvemos a seleccionar restaurar.
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )

            }
        }
    }

    // Función que se ejecutará al cambiar el ordenamiento desde la interfaz de usuario.
    // Se traen las notas según el ordenamiento recibido por parámetro.
    // Y actualizan los valores del estado reemplazando las notas y el orden (copia y reemplazo).
    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        // Asignamos la tarea y borramos la anterior para optimizar el proceso.
        getNotesJob =
        noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }




}

