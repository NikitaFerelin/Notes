package com.ferelin.notes.base

import android.view.View
import androidx.annotation.StringRes
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface AppMvpView : MvpView {

    fun hideKeyboard(view: View)

    fun showKeyboard(view: View)

    fun showMessage(@StringRes resId: Int)

    fun showMessage(message: String)
}