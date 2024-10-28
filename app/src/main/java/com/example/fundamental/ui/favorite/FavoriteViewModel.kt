package com.example.fundamental.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.fundamental.data.database.Note
import com.example.fundamental.data.repository.NoteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = NoteRepository(application)

    val favoriteNotes: LiveData<List<Note>> = noteRepository.getFavoriteNotes()
}
