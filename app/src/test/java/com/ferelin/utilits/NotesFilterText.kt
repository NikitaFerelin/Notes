package com.ferelin.utilits

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.ui.notes.NotesFilter
import com.ferelin.providers.TestDataProvider
import com.ferelin.repository.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class NotesFilterText {

    private lateinit var mNotesFilter: NotesFilter
    private lateinit var mTestDataProvider: TestDataProvider

    private var mTestList = emptyList<Note>()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mTestDataProvider = TestDataProvider(context)
        mNotesFilter = NotesFilter(mTestDataProvider.defaultNotesList) {
            mTestList = it.toList()
        }
    }

    @Test
    fun filterStrategyWithTrueQuery() {
        val note = mTestDataProvider.defaultNote

        Assert.assertEquals(true, mNotesFilter.filterStrategy(note, note.title))
        Assert.assertEquals(true, mNotesFilter.filterStrategy(note, note.preContent))
        Assert.assertEquals(true, mNotesFilter.filterStrategy(note, note.date))
    }

    @Test
    fun filterStrategyWithFalseQuery() {
        val note = mTestDataProvider.defaultNote

        Assert.assertEquals(false, mNotesFilter.filterStrategy(note, "dasda"))
        Assert.assertEquals(false, mNotesFilter.filterStrategy(note, "fadas,lgt"))
        Assert.assertEquals(false, mNotesFilter.filterStrategy(note, "dasd"))
    }

    @Test
    fun filterEmpty() {
        mNotesFilter.filter("")
        Assert.assertEquals(mTestDataProvider.defaultNotesList.size, mTestList.size)
    }

    @Test
    fun filterWithConcurrence() {
        mNotesFilter.filter(mTestDataProvider.titleQuery)
        Assert.assertEquals(mTestDataProvider.titleQueryExpected, mTestList.size)
    }

    @Test
    fun filterWithoutConcurrence() {
        mNotesFilter.filter("dasmklfgnjhegbjt")
        Assert.assertEquals(0, mTestList.size)
    }
}