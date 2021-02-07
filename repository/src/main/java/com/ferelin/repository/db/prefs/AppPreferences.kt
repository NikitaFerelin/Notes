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

    override suspend fun getLastNotePreferences(): Flow<Bundle> = mDataStorePreferences.data.map {
        val title = it[mLastNoteTitleKey] ?: ""
        val content = it[mLastNoteContentKey] ?: ""
        val color = it[mLastNoteColorKey] ?: ""
        val arguments = bundleOf(
            sTitleBundleKey to title,
            sContentBundleKey to content,
            sColorBundleKey to color
        )
        arguments
    }

    override suspend fun saveLastNotePreferences(title: String, content: String, color: String) {
        mDataStorePreferences.edit {
            it[mLastNoteTitleKey] = title
            it[mLastNoteContentKey] = content
            it[mLastNoteColorKey] = color
        }
    }

    override suspend fun clearLastNote() {
        mDataStorePreferences.edit { it.clear() }
    }

    companion object {
        const val sTitleBundleKey = "LAST_NOTE_TITLE_BUNDLE_KEY"
        const val sContentBundleKey = "LAST_NOTE_CONTENT_BUNDLE_KEY"
        const val sColorBundleKey = "LAST_NOTE_COLOR_BUNDLE_KEY"
    }
}