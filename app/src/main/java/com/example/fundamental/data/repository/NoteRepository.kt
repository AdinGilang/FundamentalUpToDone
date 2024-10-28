package com.example.fundamental.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.fundamental.data.database.Note
import com.example.fundamental.data.database.NoteDao
import com.example.fundamental.data.database.NoteRoomDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(application: Application) {
    private val notesDao: NoteDao
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        notesDao = db.noteDao()
    }

    fun getAllNotes(): LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        withContext(ioDispatcher) {
            notesDao.insert(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(ioDispatcher) {
            notesDao.delete(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(ioDispatcher) {
            notesDao.update(note)
        }
    }

    fun getFavoriteNotes(): LiveData<List<Note>> = notesDao.getFavoriteNotes()

    suspend fun getNotesByTitle(title: String): Note? {
        return withContext(ioDispatcher) {
            notesDao.getNoteByTitle(title)
        }
    }

}

