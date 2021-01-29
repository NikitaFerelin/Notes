package com.ferelin.notes.ui.details

import com.ferelin.notes.base.MvpPresenter

interface DetailsMvpPresenter<T : DetailsMvpView> : MvpPresenter<T> {

    fun onViewPrepared(args: DetailsFragmentArgs)

    fun onDeleteClicked()

    fun onBackBtnClicked()
}