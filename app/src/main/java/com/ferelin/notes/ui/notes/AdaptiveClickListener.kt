package com.ferelin.notes.ui.notes

interface AdaptiveClickListener {
    fun onItemClicked(holder: NotesAdapter.NoteViewHolder, position: Int)
}