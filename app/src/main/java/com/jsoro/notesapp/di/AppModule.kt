package com.jsoro.notesapp.di

import android.app.Application
import androidx.room.Room
import com.jsoro.notesapp.feature_note.data.data_source.NoteDatabase
import com.jsoro.notesapp.feature_note.data.repository.NoteRepositoryImpl
import com.jsoro.notesapp.feature_note.domain.repository.NoteRepository
import com.jsoro.notesapp.feature_note.domain.use_case.DeleteNote
import com.jsoro.notesapp.feature_note.domain.use_case.GetNotes
import com.jsoro.notesapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Esta clase es un objeto Kotlin que representa dentro del framework Dagger un Module.
// Los Module son aquellos componentes de la aplicación que instanciaremos al inicio.
// De forma que sólo habrá una copia en memoria de estos componentes.
// Es lo que se conoce como DI o inyección de dependencias.

// Dentro del módulo encontraremos distintas etiquetas @Provides, las cuales son los componentes
// necesarios que ofrece o inyecta este @Module

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Esta función recibe una Application y devuelve una NoteDatabase,
    // La etiqueta @Provides indica que la función instancia un componente en
    // la inyección de dependencias, en este caso una base de datos Room.
    // Esa base de datos Room se crea con una referencia a la app, la clase NoteDatabase
    // (la cual incluye el Dao para nuestros objetos Note) y el nombre establecido en dicha clase
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    // Esta función recibe una NoteDatabase y devuelve un NoteRepository. Una ventaja de la inyección
    // de dependencias es que podemos reemplazar este repositorio por otros de tipo distinto y así evitamos
    // acceder a la base de datos directamente para recibir objetos.

    // Para esos casos tendríamos un @Module diferente en el cual en vez de pasar una implementación
    // del repositorio (NoteRepositoryImpl) le pasaríamos un falso Repository, que también implementase
    // nuestra interfaz NoteRepository.
    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository):NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository)
        )
    }



}