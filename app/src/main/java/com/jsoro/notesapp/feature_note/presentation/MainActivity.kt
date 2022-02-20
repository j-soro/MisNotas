package com.jsoro.notesapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jsoro.notesapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.jsoro.notesapp.feature_note.presentation.notes.NotesScreen
import com.jsoro.notesapp.feature_note.presentation.util.Screen
import com.jsoro.notesapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

// Esta clase es el punto de entrada de la ejecución de la app. La anotación @AndroidEntryPoint es
// del framework Hilt de inyección de dependencias y su función es inyectar las dependencias que he
// configurado para que se creen en esta clase una vez se instancie la propia clase. De esta forma
// obtenemos los ViewModels, vistas y clases de dominio y casos de uso instanciadas a partir de este
// punto en la ejecución.

// Además incluye toda la navegación, la cual funciona como un gestor de elementos @Composable que
// se pasan con parámetros (route). El parámetro route sirve para indicar un contexto a la ruta, y
// se utiliza principalmente para pasar de la vista general de notas a la de editar/crear (editar en
// este caso), para enviar el Id de la nota que se vaya a editar. Se emplea la clase Screen para
// establecer destinos para la navegación en forma de rutas, preconfiguradas en esa clase para cada
// destino de nuestra capa de presentación o vista.

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // @Composable principal de la app, que contiene "content" como contenido de la misma.
            NoteAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    // Controlador de navegación, un @Composable que contiene las posibles vistas a
                    // las que podemos acceder dentro de la app y "navega" de una a otra en función
                    // del parámetro route. NavHost.
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        // @Composable de la pantalla principal con todas las notas.
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        // @Composable de la pantalla de añadir o editar nota. Se recibe el Id como
                        // parámetro para saber qué nota editar/traer del repositorio.
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    // Usando "(?)" los parámetros no son obligatorios sino que
                                    // son opcionales y no tenemos por qué pasarlos en la ruta.
                                    // Así podemos usar esta ruta tanto para nueva nota como para
                                    // notas existentes.
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                // Id de la nota, parseado de la string superior.
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                // Color de la nota, parseado de la string superior.
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            //@Composable de la pantalla de añadir/editar nota. Se le pasa
                            // una variable para el color que será -1 en caso no tener ningún color
                            // (caso de crear una nota nueva).
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}
