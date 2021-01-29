package com.ferelin.notes.ui.create

import android.os.Bundle
import android.text.Editable
import com.ferelin.notes.base.MvpPresenter

interface CreateMvpPresenter<T : CreateMvpView> : MvpPresenter<T> {

    fun onTextChanged(content: Editable?, title: Editable?, isAcceptBtnLocked: Boolean)

    fun onBackBtnClicked()

    fun onBottomSheetClicked(bottomSheetState: Int)

    fun onDefaultColorClicked()

    fun onOrangeColorClicked()

    fun onGrayColorClicked()

    fun onRedColorClicked()

    suspend fun onAcceptBtnClicked(isLocked: Boolean, title: Editable, content: Editable)

    suspend fun onViewPrepared(args: CreateFragmentArgs)

    suspend fun onSaveInstanceState(title: Editable?, content: Editable?)
}
