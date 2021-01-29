package com.ferelin.repository.db

import android.content.Context
import com.ferelin.repository.db.prefs.AppPreferencesHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.db.room.AppNotesDbHelper
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataManager private constructor(context: Context) : DataManager {

    private val mPreferencesHelper = AppPreferencesHelper(context)
    private val mRoomHelper = AppNotesDbHelper(context)

    suspend fun setLastNote(title: String, content: String, color: String) {
        mPreferencesHelper.apply {
            setLastNoteTitle(title)
            setLastNoteContent(content)
            setLastNoteColor(color)
        }
    }

    suspend fun getLastNote(): Flow<Response<Note>> = mPreferencesHelper.getLastNotePreferences().map {
        val title = it[AppPreferencesHelper.sTitleBundleKey].toString()
        val content = it[AppPreferencesHelper.sContentBundleKey].toString()
        val color = it[AppPreferencesHelper.sColorBundleKey].toString()
        if ((title.isEmpty() || content.isEmpty()) && color.isEmpty()) {
            Response.Failed()
        } else Response.Success(Note(title = title, content = content, color = color))
    }

    suspend fun clearLastNote() {
        mPreferencesHelper.clearLastNote()
    }

    override fun insertNote(note: Note) {
        mRoomHelper.insertNote(note)
    }

    override fun removeNote(note: Note) {
        mRoomHelper.removeNote(note)
    }

    override fun getNotes(): Flow<List<Note>> {
        return mRoomHelper.getNotes()
    }

    companion object {
        private var sInstance: AppDataManager? = null

        fun getInstance(context: Context): AppDataManager {
            if (sInstance == null) {
                sInstance = AppDataManager(context)
            }
            return sInstance!!
        }
    }
}