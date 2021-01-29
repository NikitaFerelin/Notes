package com.ferelin.notes.base

interface MvpPresenter<T : MvpView> {

    fun attachView(view: T)

    fun detachView()
}