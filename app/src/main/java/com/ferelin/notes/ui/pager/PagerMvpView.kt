package com.ferelin.notes.ui.pager

import android.graphics.drawable.Drawable
import com.ferelin.notes.base.MvpView

interface PagerMvpView : MvpView {

    fun changeCurrentPagerScreen(position: Int, smoothScroll: Boolean)

    fun changeIconTintToOrange(icon: Drawable)

    fun changeIconTintToGray(icon: Drawable)
}