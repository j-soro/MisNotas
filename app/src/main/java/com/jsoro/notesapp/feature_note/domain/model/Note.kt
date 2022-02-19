package com.jsoro.notesapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jsoro.notesapp.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

// Error lanzado en caso de insertar una nota que no cumpla con la validación (título vacío).
class InvalidNoteException(message: String): Exception(message)