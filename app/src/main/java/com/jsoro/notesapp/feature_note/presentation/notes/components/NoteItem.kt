package com.jsoro.notesapp.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.jsoro.notesapp.feature_note.domain.model.Note

// Esta clase fichero es un @Composable para el diseño de nuestras notas en pantalla. Dibuja una
// nota que recibe como parámetro.

// Se utiliza un Canvas para dibujar un rectángulo con las esquinas redondeadas, y entonces se usa
// un Path que lo dibuja y un clickPath que dibuja la esquina doblada de la nota en la intersección
// con el Path principal.

// Después el título y el contenido de la nota se encuentran dentro de un Column, junto con el icono
// de papelera que sirve para borrar la nota seleccionada.

// Tiene como valores-parámetros:
// - La nota a dibujar, de la que toma los valores de texto (note).
// - Modifier por defecto de Compose (modifier).
// - Radio para la curva de las esquinas de cada nota (cornerRadius).
// - Tamaño del rectángulo total, usado para recortar en la esquina superior derecha (cutCornerSize).
// - Función lambda a ejecutar al clicar el botón de papelera que borrará esa nota en concreto.
//  (onDeleteClick)

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit

) {
    Box(
        modifier = modifier
    ){

        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            // Rectángulo pequeño, esquina doblada
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(
                        (ColorUtils.blendARGB(note.color, 0x000000, 0.3f))
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .padding(end = 32.dp) // Para que no incluya el icono de la papelera dentro.
                ) {
            Text( // Título de cada nota.
                text = note.title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text( // Cuerpo de cada nota.
                text = note.content,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton( // Botón de la papelera para borrar la nota.
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Borrar nota"
            )
        }
    }
}