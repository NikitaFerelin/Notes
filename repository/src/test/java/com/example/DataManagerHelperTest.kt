package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.provider.TestDataProvider
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.db.DataManagerHelper
import com.ferelin.repository.db.prefs.AppPreferences
import com.ferelin.repository.db.prefs.PreferencesHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.db.room.AppNotesDb
import com.ferelin.repository.db.room.NotesDb
import com.ferelin.repository.db.room.NotesDbHelper
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

    private lateinit var mDatabase: NotesDb
    private lateinit var mDataManagerHelper: DataManagerHelper

    private val mTestNote = TestDataProvider().defaultNote

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appPreferencesHelper: PreferencesHelper = AppPreferences(context, "testDataStore")
        mDatabase = Room.inMemoryDatabaseBuilder(context, NotesDb::class.java).allowMainThreadQueries().build()
        val notesDbHelper = AppNotesDb(mDatabase) as NotesDbHelper
        mDataManagerHelper = AppDataManager(appPreferencesHelper, notesDbHelper)
    }

    @Test
    fun successResponse(): Unit = runBlocking {
        mDataManagerHelper.saveLastNotePreferences(mTestNote.title, mTestNote.content, mTestNote.color, mTestNote.time)
        mDataManagerHelper.getLastNote().first().also {
            Assert.assertEquals(true, it is Response.Success)

            val note = (it as Response.Success).data
            Assert.assertEquals(mTestNote.title, note.title)
            Assert.assertEquals(mTestNote.content, note.content)
            Assert.assertEquals(mTestNote.color, note.color)
            Assert.assertEquals(mTestNote.time, note.time)
        }
    }

    @Test
    fun failedResponse(): Unit = runBlocking {
        mDataManagerHelper.getLastNote().first().also {
            Assert.assertEquals(true, it is Response.Failed)
        }
    }

    @After
    fun closeDb() {
        mDatabase.close()
    }
}