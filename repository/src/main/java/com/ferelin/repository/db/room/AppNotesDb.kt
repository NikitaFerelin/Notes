package com.ferelin.repository.db.room

import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppNotesDb @Inject constructor(private val mNotesDb: NotesDb) : NotesDbHelper {

    override fun insertNote(note: Note) {
        mNotesDb.noteDao().insert(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return mNotesDb.noteDao().getAll()
    }

    override fun getNote(id: Int): Flow<Note> {
        return mNotesDb.noteDao().get(id)
    }

    override fun update(note: Note) {
        mNotesDb.noteDao().update(note)
    }

    override fun deleteNote(note: Note) {
        mNotesDb.noteDao().delete(note)
    }

    override fun deleteNote(id: Int) {
        mNotesDb.noteDao().delete(id)
    }
}