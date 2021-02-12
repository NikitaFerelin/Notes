package com.ferelin.presenters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.ui.details.DetailsMvpView
import com.ferelin.notes.ui.details.DetailsPresenter
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.providers.TestDataProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailsPresenterTest {

    private val mView: DetailsMvpView = Mockito.mock(DetailsMvpView::class.java)

    private lateinit var mPresenter: DetailsPresenter
    private lateinit var mTestDataProvider: TestDataProvider

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mPresenter = DetailsPresenter(context).apply {
            attachView(mView)
        }
        mTestDataProvider = TestDataProvider(context)
    }

    @Test
    fun isAttached() {
        Assert.assertEquals(true, mPresenter.attachedViews.contains(mView))
    }

    @Test
    fun onViewPrepared() {
        val args = mTestDataProvider.detailsFragmentArgs
        val note = args.note

        mPresenter.onViewPrepared(args)
        Mockito.verify(mView, Mockito.times(1)).setContent(note.content)
        Mockito.verify(mView, Mockito.times(1)).setTitle(note.title)
        Mockito.verify(mView, Mockito.times(1)).setDate(note.date)
        Mockito.verify(mView, Mockito.times(1)).setReminder(note.time)
        Mockito.verify(mView, Mockito.times(1)).setColor(ColorTransformer.fromStringToInt(note.color))
    }

    @Test
    fun onDeleteClicked() {
        val args = mTestDataProvider.detailsFragmentArgs

        mPresenter.onViewPrepared(args)
        mPresenter.onDeleteClicked()
        // TODO Different Arguments error. Cannot compare bundles
        // Mockito.verify(mView, Mockito.times(1)).setResult(bundleOf(), args.responseKey)
        Mockito.verify(mView, Mockito.times(1)).dismiss()
    }

    @Test
    fun onBackBtnClicked() {
        mPresenter.onBackBtnClicked()
        Mockito.verify(mView, Mockito.times(1)).dismiss()
    }
}