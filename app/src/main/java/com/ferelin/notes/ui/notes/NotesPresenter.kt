package com.ferelin.notes.ui.notes

import android.os.Bundle
import android.text.Editable
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.repository.db.DataManagerHelper
import com.ferelin.repository.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class NotesPresenter(private val mDataManager: DataManagerHelper) : MvpPresenter<NotesMvpView>() {

    fun onFragmentCreate() {
        viewState.apply {
            setUpCreateFrgResultListener()
            setUpDetailFrgResultListener()
        }
    }

    fun onFilterInitialized(searchText: Editable) {
        if (searchText.toString().isNotEmpty()) {
            viewState.triggerFilter()
        }
    }

    fun onResultsPublished(items: List<Note>) {
        viewState.setNotes(items)
    }

    fun onSearchTextChanged(isFieldFocused: Boolean, text: Editable?) {
        if (isFieldFocused) {
            viewState.filter(text.toString())
        }
    }

    suspend fun getNotes(): Flow<List<Note>> = flow {
        mDataManager.getNotes().collect {
            emit(it)
        }
    }

    suspend fun gotResultFromDetailsFrg(lastClickedNote: Note) {
        withContext(Dispatchers.Main) {
            viewState.apply {
                removeLastClickedNote()
                removeNoteFromFilter(lastClickedNote)
                triggerFilter()
            }
        }
        mDataManager.removeNote(lastClickedNote)
    }

    suspend fun gotResultFromCreateFrg(bundle: Bundle) {
        val title = bundle[CreatePresenter.NOTE_TITLE_KEY] as String
        val content = bundle[CreatePresenter.NOTE_CONTENT_KEY] as String
        val color = bundle[CreatePresenter.NOTE_COLOR_KEY] as String
        val newNote = Note(title = title, content = content, color = color)

        withContext(Dispatchers.Main) {
            viewState.apply {
                addNote(newNote)
                addNoteToFilter(newNote)
            }
        }
        mDataManager.insertNote(newNote)
    }

    fun onFabClicked() {
        viewState.replaceWithCreateFragment()
    }

    fun onNoteClicked(holder: NotesAdapter.NoteViewHolder, note: Note) {
        viewState.replaceWithDetailFragment(holder, note.title, note.content, note.date, note.color)
    }
}