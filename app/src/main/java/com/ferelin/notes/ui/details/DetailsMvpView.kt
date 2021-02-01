package com.ferelin.notes.ui.details

import android.os.Bundle
import com.ferelin.notes.base.AppMvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DetailsMvpView : AppMvpView {

    fun dismiss()

    fun setTitle(title: String)

    fun setContent(content: String)

    fun setDate(date: String)

    fun setColor(color: Int)

    fun setResult(bundle: Bundle, responseKey: String)
}