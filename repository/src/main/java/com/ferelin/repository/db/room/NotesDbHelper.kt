package com.ferelin.repository.db.room

import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesDbHelper {

    fun insertNote(note: Note)

    fun getNotes(): Flow<List<Note>>

    fun getNote(id: Int): Flow<Note>

    fun update(note: Note)

    fun deleteNote(note: Note)

    fun deleteNote(id: Int)
}