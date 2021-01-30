package com.ferelin.notes.ui.notes

import android.content.Context
import android.os.Bundle
import com.ferelin.notes.base.BasePresenter
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NotesPresenter<T : NotesMvpView>(context: Context) : BasePresenter<T>(), NotesMvpPresenter<T> {

    private val mDataManager = AppDataManager.getInstance(context)

    override suspend fun getNotes(): Flow<List<Note>> = flow {
        mDataManager.getNotes().collect {
            emit(it)
        }
    }

    override suspend fun gotResultFromDetails(lastClickedNote: Note) {
        withContext(Dispatchers.Main) {
            view.removeLastClickedNote()
        }
        mDataManager.removeNote(lastClickedNote)
    }

    override suspend fun gotResultFromCreate(bundle: Bundle) {
        val title = bundle[CreatePresenter.NOTE_TITLE_KEY] as String
        val content = bundle[CreatePresenter.NOTE_CONTENT_KEY] as String
        val color = bundle[CreatePresenter.NOTE_COLOR_KEY] as String
        val newNote = Note(title = title, content = content, color = color)

        withContext(Dispatchers.Main) {
            view.addNote(newNote)
        }
        mDataManager.insertNote(newNote)
    }

    override fun onFabClicked() {
        view.moveToCreateNote()
    }

    override fun onNoteClicked(holder: NotesAdapter.NoteViewHolder, position: Int) {
        val note = view.getNote(position)
        view.moveToNoteInfo(holder, note.title, note.content, note.date, note.color)
    }
}