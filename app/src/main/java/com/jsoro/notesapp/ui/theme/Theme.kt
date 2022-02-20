package com.jsoro.notesapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = DarkGray,
    onBackground = Color.White,
    surface = LightBlue,
    onSurface = DarkGray
)

private val LightColorPalette = darkColors(
    primary = Color.Black,
    background = Color.LightGray,
    onBackground = Color.Gray,
    surface = LightBlue,
    onSurface = Color.Blue
)

// A esta función @Composable le pasamos los estilos deseados para nuestra app
// además del contenido y es la responsable de generar toda la composición visual
// a partir de los distintos componentes de cada vista.

@Composable
fun NoteAppTheme(darkTheme: Boolean = true, content: @Composable() () -> Unit) {

    MaterialTheme(
        colors =  if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}