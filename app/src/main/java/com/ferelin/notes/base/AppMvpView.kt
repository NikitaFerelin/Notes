package com.ferelin.notes.base

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface AppMvpView : MvpView {

    fun hideKeyboard(context: Context, view: View)

    fun showKeyboard(context: Context, view: View)

    fun showMessage(root: View, @StringRes resId: Int)

    fun showMessage(root: View, message: String)
}