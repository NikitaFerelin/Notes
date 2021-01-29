package com.ferelin.notes.ui.notes

import android.os.Bundle
import com.ferelin.notes.base.MvpPresenter
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesMvpPresenter<T : NotesMvpView> : MvpPresenter<T> {

    fun onFabClicked()

    fun onNoteClicked(holder: NotesAdapter.NoteViewHolder, position: Int)

    suspend fun gotResultFromDetails(lastClickedNote: Note)

    suspend fun gotResultFromCreate(bundle: Bundle)

    suspend fun getNotes(): Flow<List<Note>>
}