package com.ferelin.repository.db.prefs

import android.os.Bundle
import kotlinx.coroutines.flow.Flow

interface PreferencesHelper {

    suspend fun getLastNotePreferences(): Flow<Bundle>

    suspend fun clearLastNote()

    suspend fun setLastNoteTitle(title: String)

    suspend fun setLastNoteContent(content: String)

    suspend fun setLastNoteColor(color: String)
}