package com.ferelin.repository.db

import android.os.Bundle
import com.ferelin.repository.db.prefs.AppPreferences
import com.ferelin.repository.db.prefs.PreferencesHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.db.room.NotesDbHelper
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppDataManager @Inject constructor(
    private val mPreferencesHelper: PreferencesHelper,
    private val mNotesDbHelper: NotesDbHelper,
) : DataManagerHelper {

    override suspend fun getLastNote(): Flow<Response<Note>> = mPreferencesHelper.getLastNotePreferences().map {
        val title = it[AppPreferences.sTitleBundleKey].toString()
        val content = it[AppPreferences.sContentBundleKey].toString()
        val color = it[AppPreferences.sColorBundleKey].toString()
        if ((title.isEmpty() || content.isEmpty()) && color.isEmpty()) {
            Response.Failed()
        } else Response.Success(Note(title = title, content = content, color = color))
    }

    override suspend fun saveLastNotePreferences(title: String, content: String, color: String) {
        mPreferencesHelper.saveLastNotePreferences(title, content, color)
    }

    override suspend fun getLastNotePreferences(): Flow<Bundle> {
        return mPreferencesHelper.getLastNotePreferences()
    }

    override suspend fun clearLastNote() {
        mPreferencesHelper.clearLastNote()
    }

    override fun update(note: Note) {
        mNotesDbHelper.update(note)
    }

    override fun insertNote(note: Note) {
        mNotesDbHelper.insertNote(note)
    }

    override fun deleteNote(id: Int) {
        mNotesDbHelper.deleteNote(id)
    }

    override fun deleteNote(note: Note) {
        mNotesDbHelper.deleteNote(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return mNotesDbHelper.getNotes()
    }

    override fun getNote(id: Int): Flow<Note> {
        return mNotesDbHelper.getNote(id)
    }
}