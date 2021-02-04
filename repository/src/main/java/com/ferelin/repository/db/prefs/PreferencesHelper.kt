package com.ferelin.repository.db.prefs

import android.os.Bundle
import kotlinx.coroutines.flow.Flow

interface PreferencesHelper {

    suspend fun getLastNotePreferences(): Flow<Bundle>

    suspend fun setLastNotePreferences(title: String, content: String, color: String)

    suspend fun clearLastNote()
}