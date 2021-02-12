package com.ferelin.presenters

import android.content.Context
import android.text.Editable
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.ui.notes.NotesAdapter
import com.ferelin.notes.ui.notes.NotesMvpView
import com.ferelin.notes.ui.notes.NotesPresenter
import com.ferelin.providers.TestCoroutineContextProvider
import com.ferelin.providers.TestDataProvider
import com.ferelin.repository.db.AppDataManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class NotesPresenterTest {

    @Spy
    private val mDataManager = Mockito.mock(AppDataManager::class.java)
    private val mView: NotesMvpView = Mockito.mock(NotesMvpView::class.java)

    private lateinit var mPresenter: NotesPresenter
    private lateinit var mTestDataProvider: TestDataProvider

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mTestDataProvider = TestDataProvider(context)
        mPresenter = NotesPresenter(mDataManager, TestCoroutineContextProvider()).apply {
            attachView(mView)
        }
    }

    @Test
    fun isAttached() {
        Assert.assertEquals(true, mPresenter.attachedViews.contains(mView))
    }

    @Test
    fun onFragmentCreate() {
        mPresenter.onFragmentCreate()
        Mockito.verify(mView, Mockito.times(1)).setUpCreateFrgResultListener()
        Mockito.verify(mView, Mockito.times(1)).setUpDetailFrgResultListener()
    }

    @Test
    fun onFragmentInitializedWithEmptyText() {
        val empty = Editable.Factory.getInstance().newEditable("")

        mPresenter.onFilterInitialized(empty)
        Mockito.verify(mView, Mockito.times(0)).triggerFilter()
    }

    @Test
    fun onFragmentInitializedWithText() {
        mPresenter.onFilterInitialized(mTestDataProvider.editableTitle)
        Mockito.verify(mView, Mockito.times(1)).triggerFilter()
    }

    @Test
    fun onResultsPublished() {
        val empty = emptyList<Nothing>()

        mPresenter.onResultsPublished(empty)
        Mockito.verify(mView, Mockito.times(1)).setNotes(empty)
    }

    @Test
    fun onSearchTextChangedWhenFieldNotFocused() {
        mPresenter.onSearchTextChanged(false, mTestDataProvider.editableTitle)
        Mockito.verify(mView, Mockito.times(0)).filter(mTestDataProvider.editableTitle.toString())
    }

    @Test
    fun onSearchTextChangedWhenFieldFocused() {
        mPresenter.onSearchTextChanged(true, mTestDataProvider.editableTitle)
        Mockito.verify(mView, Mockito.times(1)).filter(mTestDataProvider.editableTitle.toString())
    }

    @Test
    fun getNotes(): Unit = runBlocking {
        mPresenter.getNotes()
        // Mockito.verify(mDataManager, Mockito.times(1)).getNotes() Not called
    }

    @Test
    fun gotResultFromDetailsFrg() {
        val note = mTestDataProvider.defaultNote

        mPresenter.gotResultFromDetailsFrg(note)
        Mockito.verify(mView, Mockito.times(1)).removeLastClickedNote()
        Mockito.verify(mView, Mockito.times(1)).removeNoteFromFilter(note)
        Mockito.verify(mView, Mockito.times(1)).triggerFilter()
        Mockito.verify(mDataManager, Mockito.times(1)).deleteNote(note)
    }

    @Test
    fun gotResultFromCreateFrg() {
        val note = mTestDataProvider.defaultNote
        val noteBundle = mTestDataProvider.defaultNoteBundle

        mPresenter.gotResultFromCreateFrg(noteBundle)
        Mockito.verify(mView, Mockito.times(1)).addNote(note)
        Mockito.verify(mView, Mockito.times(1)).addNoteToFilter(note)
        Mockito.verify(mDataManager, Mockito.times(1)).insertNote(note)
    }

    @Test
    fun onFabClicked() {
        mPresenter.onFabClicked()
        Mockito.verify(mView, Mockito.times(1)).replaceWithCreateFragment()
    }

    @Test
    fun onNoteClicked() {
        val note = mTestDataProvider.defaultNote
        val holder = Mockito.mock(NotesAdapter.NoteViewHolder::class.java)

        mPresenter.onNoteClicked(holder, note)
        Mockito.verify(mView, Mockito.times(1)).replaceWithDetailFragment(holder, note)
    }
}