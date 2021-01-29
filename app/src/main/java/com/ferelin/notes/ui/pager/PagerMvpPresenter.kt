package com.ferelin.notes.ui.pager

import com.ferelin.notes.base.MvpPresenter
import com.google.android.material.tabs.TabLayout

interface PagerMvpPresenter<T : PagerMvpView> : MvpPresenter<T> {

    fun onTabSelected(tab: TabLayout.Tab)

    fun onTabUnselected(tab: TabLayout.Tab)
}