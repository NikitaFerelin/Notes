package com.ferelin.repository.db.room

import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppNotesDb @Inject constructor(private val mNotesDb: NotesDb) : NotesDbHelper {

    override fun insertNote(note: Note) {
        mNotesDb.noteDao().insert(note)
    }

    override fun removeNote(note: Note) {
        mNotesDb.noteDao().delete(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return mNotesDb.noteDao().getAll()
    }
}