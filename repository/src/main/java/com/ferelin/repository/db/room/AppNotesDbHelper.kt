package com.ferelin.repository.db.room

import android.content.Context
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

class AppNotesDbHelper(context: Context) : NotesDbHelper {

    private val mNotesRoom = NotesDb.getDatabase(context).noteDao()

    override fun insertNote(note: Note) {
        mNotesRoom.insert(note)
    }

    override fun removeNote(note: Note) {
        mNotesRoom.delete(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return mNotesRoom.getAll()
    }
}