package com.ferelin.notes.ui.create

import android.os.Bundle
import com.ferelin.notes.base.AppMvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateMvpView : AppMvpView {

    fun hideKeyboard()

    fun showKeyboard()

    fun lockAcceptButton()

    fun unlockAcceptButton()

    fun expandBottomSheet()

    fun collapseBottomSheet()

    fun setFocusToContentEdit()

    fun setResult(arg: Bundle, responseKey: String)

    fun setNote(title: String, content: String)

    fun setSelectedColor(color: Int)

    fun setReminderTime(time: String)

    fun changeIconConstraintsToDefault()

    fun changeIconConstraintsToRed()

    fun changeIconConstraintsToGray()

    fun changeIconConstraintsToOrange()

    fun showDatePickerDialog(year: Int, month: Int, day: Int)

    fun showTimePickerDialog(hour: Int, minute: Int)

    fun dismiss()
}