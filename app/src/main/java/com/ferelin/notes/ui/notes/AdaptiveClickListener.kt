package com.ferelin.notes.ui.notes

import com.ferelin.repository.model.Note
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AdaptiveClickListener {
    fun onItemClicked(holder: NotesAdapter.NoteViewHolder, note: Note)
}