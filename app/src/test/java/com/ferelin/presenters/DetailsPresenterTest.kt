package com.ferelin.presenters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.ui.details.DetailsMvpView
import com.ferelin.notes.ui.details.DetailsPresenter
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.providers.AppTestingDataProvider
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
    private lateinit var mTestDataProvider: AppTestingDataProvider

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mPresenter = DetailsPresenter(context).apply {
            attachView(mView)
        }
        mTestDataProvider = AppTestingDataProvider(context)
    }

    @Test
    fun isAttached() {
        Assert.assertEquals(true, mPresenter.attachedViews.contains(mView))
    }

    @Test
    fun onViewPrepared() {
        val args = mTestDataProvider.detailsFragmentArgs

        mPresenter.onViewPrepared(args)
        Mockito.verify(mView, Mockito.times(1)).setContent(args.content)
        Mockito.verify(mView, Mockito.times(1)).setTitle(args.title)
        Mockito.verify(mView, Mockito.times(1)).setDate(args.date)
        Mockito.verify(mView, Mockito.times(1)).setColor(ColorTransformer.fromStringToInt(args.color))
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