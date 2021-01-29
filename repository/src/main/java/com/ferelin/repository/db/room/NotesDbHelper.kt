package com.ferelin.repository.db.room

import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesDbHelper {

    fun insertNote(note: Note)

    fun removeNote(note: Note)

    fun getNotes(): Flow<List<Note>>
}