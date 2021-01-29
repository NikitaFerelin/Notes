package com.ferelin.notes.ui.notes

import com.ferelin.notes.base.MvpView
import com.ferelin.repository.model.Note

interface NotesMvpView : MvpView, AdaptiveClickListener {

    fun moveToCreateNote()

    fun moveToNoteInfo(holder: NotesAdapter.NoteViewHolder, title: String, content: String, date: String, color: String)

    fun addNote(note: Note)

    fun removeLastClickedNote()

    fun getNote(position: Int): Note
}