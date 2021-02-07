package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.db.DataManagerHelper
import com.ferelin.repository.db.prefs.AppPreferences
import com.ferelin.repository.db.prefs.PreferencesHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.db.room.AppNotesDb
import com.ferelin.repository.db.room.NotesDb
import com.ferelin.repository.db.room.NotesDbHelper
import com.ferelin.repository.model.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DataManagerHelperTest {

    private lateinit var mDataManagerHelper: DataManagerHelper
    private lateinit var mDatabase: NotesDb
    private lateinit var mTestNote: Note

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appPreferencesHelper: PreferencesHelper = AppPreferences(context, "testDataStore")
        mDatabase = NotesDb.getDatabase(context)
        val notesDbHelper: NotesDbHelper = AppNotesDb(mDatabase)

        mDataManagerHelper = AppDataManager(appPreferencesHelper, notesDbHelper)
        mTestNote = Note(title = "TestNote", content = "TestContent", color = "#000000")
    }

    @Test
    fun successResponse() {
        runBlocking {
            mDataManagerHelper.saveLastNotePreferences(mTestNote.title, mTestNote.content, mTestNote.color)
            mDataManagerHelper.getLastNote().first().also {
                Assert.assertEquals(true, it is Response.Success)

                val note = (it as Response.Success).data
                Assert.assertEquals(mTestNote.title, note.title)
                Assert.assertEquals(mTestNote.content, note.content)
                Assert.assertEquals(mTestNote.color, note.color)
            }
        }
    }

    @Test
    fun failedResponse() {
        runBlocking {
            mDataManagerHelper.getLastNote().first().also {
                Assert.assertEquals(true, it is Response.Failed)
            }
        }
    }

    @After
    fun closeDb() {
        mDatabase.close()
    }
}