package com.ferelin.presenters

import android.content.Context
import android.text.Editable
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.ui.create.CreateMvpView
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.providers.AppTestingDataProvider
import com.ferelin.providers.TestingCoroutineContextProvider
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.db.response.Response
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class CreatePresenterTest {

    @Spy
    private val mDataManager = mock(AppDataManager::class.java)
    private val mView: CreateMvpView = mock(CreateMvpView::class.java)

    private lateinit var mPresenter: CreatePresenter
    private lateinit var mTestDataProvider: AppTestingDataProvider

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mPresenter = CreatePresenter(context, mDataManager, TestingCoroutineContextProvider()).apply {
            attachView(mView)
        }
        mTestDataProvider = AppTestingDataProvider(context)
    }

    @Test
    fun isAttached() {
        Assert.assertEquals(true, mPresenter.attachedViews.contains(mView))
    }

    @Test
    fun onViewPreparedWithSuccessRecoveredNote() = runBlocking {
        val args = mTestDataProvider.createFragmentArgs
        val note = mTestDataProvider.defaultNote

        `when`(mDataManager.getLastNote()).thenReturn(flowOf(Response.Success(note)))

        mPresenter.onViewPrepared(args)
        verify(mDataManager, times(1)).getLastNote()
        verify(mView, times(1)).setFocusToContentEdit()
        verify(mView, times(1)).showKeyboard()
        verify(mView, times(1)).setNote(note.title, note.content)
        verify(mView, times(1)).setSelectedColor(ColorTransformer.fromStringToInt(note.color))
        verify(mView, times(1)).changeIconConstraintsToRed()
    }

    @Test
    fun onViewPreparedWithFailedRecoveredNote() = runBlocking {
        val args = mTestDataProvider.createFragmentArgs

        `when`(mDataManager.getLastNote()).thenReturn(flowOf(Response.Failed()))

        mPresenter.onViewPrepared(args)
        verify(mDataManager, times(1)).getLastNote()
        verify(mView, times(1)).setFocusToContentEdit()
        verify(mView, times(1)).showKeyboard()
        verify(mView, times(0)).setNote("", "")
        verify(mView, times(0)).setSelectedColor(0)
        verify(mView, times(0)).changeIconConstraintsToDefault()
        verify(mView, times(0)).changeIconConstraintsToRed()
        verify(mView, times(0)).changeIconConstraintsToOrange()
        verify(mView, times(0)).changeIconConstraintsToGray()
    }

    @Test
    fun onAcceptBtnClickedWhenBtnIsLocked() {
        val editableTitle = mTestDataProvider.editableTitle
        val editableContent = mTestDataProvider.editableContent
        val errorMessage = mTestDataProvider.noteIsEmptyMessage

        mPresenter.onAcceptBtnClicked(true, editableTitle, editableContent)
        verify(mView, times(1)).showMessage(errorMessage)
        verify(mView, times(0)).hideKeyboard()
    }

    @Test
    fun onAcceptBtnClickedWhenBtnIsUnlocked() = runBlocking {
        val args = mTestDataProvider.createFragmentArgs
        val note = mTestDataProvider.defaultNote
        val editableTitle = mTestDataProvider.editableTitle
        val editableContent = mTestDataProvider.editableContent
        val responseKey = mTestDataProvider.defaultResponseKey

        `when`(mDataManager.getLastNote()).thenReturn(flowOf(Response.Success(note)))
        mPresenter.onViewPrepared(args)

        mPresenter.onAcceptBtnClicked(false, editableTitle, editableContent)
        verify(mView, times(0)).showMessage("")
        // verify(mView, times(1)).setResult(transformedNoteBundle, responseKey)
        verify(mView, times(1)).hideKeyboard()
        verify(mView, times(1)).dismiss()
    }

    @Test
    fun onBottomSheetClickedWhenIsCollapsed() {
        mPresenter.onBottomSheetClicked(BottomSheetBehavior.STATE_COLLAPSED)
        verify(mView, times(1)).expandBottomSheet()
        verify(mView, times(0)).collapseBottomSheet()
    }

    @Test
    fun onBottomSheetClickedWhenIsExpanded() {
        mPresenter.onBottomSheetClicked(BottomSheetBehavior.STATE_EXPANDED)
        verify(mView, times(1)).collapseBottomSheet()
        verify(mView, times(0)).expandBottomSheet()
    }

    @Test
    fun onDefaultColorClicked() {
        mPresenter.onDefaultColorClicked()
        verify(mView, times(1)).changeIconConstraintsToDefault()
    }

    @Test
    fun onOrangeColorClicked() {
        mPresenter.onOrangeColorClicked()
        verify(mView, times(1)).changeIconConstraintsToOrange()
    }

    @Test
    fun onGrayColorClicked() {
        mPresenter.onGrayColorClicked()
        verify(mView, times(1)).changeIconConstraintsToGray()
    }

    @Test
    fun onRedColorClicked() {
        mPresenter.onRedColorClicked()
        verify(mView, times(1)).changeIconConstraintsToRed()
    }

    @Test
    fun onBackBtnClicked() {
        mPresenter.onBackBtnClicked()
        verify(mView, times(1)).hideKeyboard()
        verify(mView, times(1)).dismiss()
    }

    @Test
    fun onSaveInstanceState() = runBlocking {
        val title = mTestDataProvider.defaultNote.title
        val content = mTestDataProvider.defaultNote.content
        val color = mTestDataProvider.defaultColor
        val editableTitle = mTestDataProvider.editableTitle
        val editableContent = mTestDataProvider.editableContent

        mPresenter.onSaveInstanceState(editableTitle, editableContent)
        verify(mDataManager, times(1)).saveLastNotePreferences(title, content, color)
    }

    @Test
    fun onTextChangedWithCorrect() {
        mPresenter.onTextChanged(mTestDataProvider.editableTitle, mTestDataProvider.editableContent, true)
        verify(mView, times(1)).unlockAcceptButton()
    }

    @Test
    fun onTextChangedWithNullsOrEmpty() {
        val empty = Editable.Factory.getInstance().newEditable("")

        mPresenter.onTextChanged(null, null, false)
        mPresenter.onTextChanged(empty, empty, false)
        verify(mView, times(2)).lockAcceptButton()
    }

    @Test
    fun changeColorWithAlreadySelectedColor() {
        mPresenter.onOrangeColorClicked() // set init color -> one call view.setSelectedColor()
        mPresenter.onOrangeColorClicked() // set again init color -> should not called view.setSelectedColor()
        verify(mView, times(1)).setSelectedColor(any(Int::class.java))
    }

    @After
    fun clear() {
        mPresenter.detachView(mView)
    }
}