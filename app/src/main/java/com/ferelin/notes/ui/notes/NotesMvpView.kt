package com.ferelin.notes.ui.notes

import com.ferelin.notes.base.MvpView
import com.ferelin.repository.model.Note

interface NotesMvpView : MvpView, AdaptiveClickListener {

    fun replaceWithCreateFragment()

    fun replaceWithDetailFragment(holder: NotesAdapter.NoteViewHolder, title: String, content: String, date: String, color: String)

    fun addNote(note: Note)

    fun removeLastClickedNote()

    fun removeNoteFromFilter(note: Note)

    fun addNoteToFilter(note: Note)

    fun triggerFilter()

    fun filter(text: String)

    fun getNote(position: Int): Note
}