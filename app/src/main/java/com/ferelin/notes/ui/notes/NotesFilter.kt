package com.ferelin.notes.ui.notes

import com.ferelin.notes.utilits.AppFilter
import com.ferelin.repository.model.Note

class NotesFilter(items: List<Note>, val onResultsPublished: (results: List<Note>) -> Unit) : AppFilter<Note>() {

    override val items: MutableList<Note> = items.toMutableList()

    override fun filterStrategy(item: Note, text: String): Boolean {
        return item.title.toLowerCase(java.util.Locale.ROOT).contains(text) ||
                item.preContent.toLowerCase(java.util.Locale.ROOT).contains(text) ||
                item.date.toLowerCase(java.util.Locale.ROOT).contains(text)
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        onResultsPublished(results!!.values as List<Note>)
    }
}