package com.ferelin.repository.db.prefs

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class AppPreferences @Inject constructor(context: Context, dataStoreName: String) : PreferencesHelper {

    private val mDataStorePreferences: DataStore<Preferences> = context.createDataStore(dataStoreName)

    private val mLastNoteTitleKey = stringPreferencesKey("LAST_NOTE_TITLE_KEY")
    private val mLastNoteContentKey = stringPreferencesKey("LAST_NOTE_CONTENT_KEY")
    private val mLastNoteColorKey = stringPreferencesKey("LAST_NOTE_COLOR_KEY")
    private val mLastNoteTimeKey = stringPreferencesKey("LAST_NOTE_TIME_KEY")

    override suspend fun getLastNotePreferences(): Flow<Bundle> = mDataStorePreferences.data.map {
        val title = it[mLastNoteTitleKey] ?: ""
        val content = it[mLastNoteContentKey] ?: ""
        val color = it[mLastNoteColorKey] ?: ""
        val time = it[mLastNoteTimeKey] ?: ""
        val arguments = bundleOf(
            TITLE_BUNDLE_KEY to title,
            CONTENT_BUNDLE_KEY to content,
            COLOR_BUNDLE_KEY to color,
            TIME_BUNDLE_KEY to time
        )
        arguments
    }

    override suspend fun saveLastNotePreferences(title: String, content: String, color: String, time: String) {
        mDataStorePreferences.edit {
            it[mLastNoteTitleKey] = title
            it[mLastNoteContentKey] = content
            it[mLastNoteColorKey] = color
            it[mLastNoteTimeKey] = time
        }
    }

    override suspend fun clearLastNote() {
        mDataStorePreferences.edit { it.clear() }
    }

    companion object {
        const val TITLE_BUNDLE_KEY = "LAST_NOTE_TITLE_BUNDLE_KEY"
        const val CONTENT_BUNDLE_KEY = "LAST_NOTE_CONTENT_BUNDLE_KEY"
        const val COLOR_BUNDLE_KEY = "LAST_NOTE_COLOR_BUNDLE_KEY"
        const val TIME_BUNDLE_KEY = "LAST_NOTE_TIME_BUNDLE_KEY"
    }
}