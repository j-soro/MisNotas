package com.jsoro.notesapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

// Esta clase contiene los eventos para cada acción de la interfaz de usuario que podrá realizarse
// en la pantalla de crear/editar una nota (AddEditNoteViewModel). La diferencia con los campos
// presentes en el propio ViewModel es que aquí hemos creado una plantilla para lanzar un evento de
// cambio en la interfaz de usuario, por ejemplo si se ha introducido algún texto en el título o en
// en el cuerpo de la nota, si ha cambiado ese texto o si se ha presionado el botón de guardar nota.


sealed class AddEditNoteEvent{
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangedTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangedContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()




}

