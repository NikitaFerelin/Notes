package com.ferelin.repository.db

import com.ferelin.repository.db.prefs.PreferencesHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.db.room.NotesDbHelper
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.Flow

interface DataManagerHelper : NotesDbHelper, PreferencesHelper {
    suspend fun getLastNote(): Flow<Response<Note>>
}