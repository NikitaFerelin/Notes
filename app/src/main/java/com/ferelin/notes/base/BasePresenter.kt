package com.ferelin.notes.base

abstract class BasePresenter<T : MvpView> : MvpPresenter<T> {

    private var mView: T? = null
    val view: T
        get() = mView ?: throw NullPointerException("View is not attach to presenter.")

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }
}