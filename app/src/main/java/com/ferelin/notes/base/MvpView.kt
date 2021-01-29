package com.ferelin.notes.base

import android.view.View
import androidx.annotation.StringRes

interface MvpView {

    fun hideKeyboard()

    fun showKeyboard(view: View)

    fun showMessage(@StringRes resId: Int)

    fun showMessage(message: String)
}