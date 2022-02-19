package com.jsoro.notesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Esta clase es la aplicación en sí, se emplea la anotación @HiltAndroidApp
// para indicar que contendrá todos los componentes de Dagger (que es el framework)
// de inyección de dependencias que estamos usando).

// Para poder inyectar dependencias debemos incluir esta clase en el archivo
// AndroidManifest.xml con el atributo android:name=".NombreClase".
// Además tendremos un módulo DI con singletons para cada clase que queramos inyectar.

@HiltAndroidApp
class NoteApp : Application()