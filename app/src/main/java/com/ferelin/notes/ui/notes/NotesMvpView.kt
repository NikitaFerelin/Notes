package com.ferelin.notes.ui.notes

import com.ferelin.notes.base.AppMvpView
import com.ferelin.repository.model.Note
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NotesMvpView : AppMvpView, AdaptiveClickListener {

    fun setUpDetailFrgResultListener()

    fun setUpCreateFrgResultListener()

    fun replaceWithCreateFragment()

    fun replaceWithDetailFragment(holder: NotesAdapter.NoteViewHolder, note: Note)

    fun setNotes(items: List<Note>)

    fun addNote(note: Note)

    fun removeLastClickedNote()

    fun removeNoteFromFilter(note: Note)

    fun addNoteToFilter(note: Note)

    fun triggerFilter()

    fun filter(text: String)
}