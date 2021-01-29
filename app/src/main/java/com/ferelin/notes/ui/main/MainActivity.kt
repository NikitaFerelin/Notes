package com.ferelin.notes.ui.main

import android.os.Bundle
import com.ferelin.notes.R
import com.ferelin.notes.base.BaseActivity

class MainActivity : BaseActivity(), MainMvpView {

    private val mPresenter = MainPresenter<MainMvpView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }
}