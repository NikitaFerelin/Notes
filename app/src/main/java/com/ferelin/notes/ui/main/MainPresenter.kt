package com.ferelin.notes.ui.main

import com.ferelin.notes.base.BasePresenter

class MainPresenter<T : MainMvpView> : BasePresenter<T>(), MainMvpPresenter<T> {

}