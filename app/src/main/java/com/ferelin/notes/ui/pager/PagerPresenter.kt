package com.ferelin.notes.ui.pager

import com.ferelin.notes.base.BasePresenter
import com.google.android.material.tabs.TabLayout

class PagerPresenter<T : PagerMvpView> : BasePresenter<T>(), PagerMvpPresenter<T> {

    override fun onTabSelected(tab: TabLayout.Tab) {
        view.apply {
            changeCurrentPagerScreen(tab.position, true)
            changeIconTintToOrange(tab.icon!!)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        view.changeIconTintToGray(tab.icon!!)
    }
}