package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.provider.RepTestingDataProvider
import com.ferelin.repository.db.room.AppNotesDb
import com.ferelin.repository.db.room.NotesDb
import com.ferelin.repository.db.room.NotesDbHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class RoomHelperTest {

    private lateinit var mDatabase: NotesDb
    private lateinit var mDatabaseHelper: NotesDbHelper

    private val mTestNote = RepTestingDataProvider().defaultNote
    private val mTestDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mDatabase = Room.inMemoryDatabaseBuilder(context, NotesDb::class.java)
            .setTransactionExecutor(mTestDispatcher.asExecutor())
            .setQueryExecutor(mTestDispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()
        mDatabaseHelper = AppNotesDb(mDatabase)
    }

    @Test
    fun insert(): Unit = runBlocking {
        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.getNotes().first().also {
            val note = it[0]
            Assert.assertEquals(note.title, mTestNote.title)
            Assert.assertEquals(note.content, mTestNote.content)
            Assert.assertEquals(note.color, mTestNote.color)
        }
    }

    @Test
    fun getNotes(): Unit = runBlocking {
        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.getNotes().first().also {
            Assert.assertEquals(1, it.size)
            Assert.assertEquals(mTestNote.title, it[0].title)
            Assert.assertEquals(mTestNote.content, it[0].content)
        }
    }

    @Test
    fun getNoteById(): Unit = runBlocking {
        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.getNotes().first().also {
            Assert.assertEquals(mTestNote.id, it[0].id)
        }
    }

    @Test
    fun update(): Unit = runBlocking {
        val newNote = mTestNote.copy(title = "NewTitle")

        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.update(newNote)
        mDatabaseHelper.getNotes().first().also {
            Assert.assertEquals(newNote.title, it[0].title)
        }
    }

    @Test
    fun removeNote(): Unit = runBlocking {
        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.deleteNote(mTestNote)
        mDatabaseHelper.getNotes().first().also {
            Assert.assertEquals(true, it.isEmpty())
        }

    }

    @Test
    fun removeNoteById(): Unit = runBlocking {
        mDatabaseHelper.insertNote(mTestNote)
        mDatabaseHelper.deleteNote(mTestNote.id)
        mDatabaseHelper.getNotes().first().also {
            Assert.assertEquals(true, it.isEmpty())
        }
    }

    @After
    fun closeDb() {
        mDatabase.close()
    }
}