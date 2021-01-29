package com.ferelin.notes.ui.details

import android.os.Bundle
import com.ferelin.notes.base.MvpView

interface DetailsMvpView : MvpView {

    fun dismiss()

    fun setTitle(title: String)

    fun setContent(content: String)

    fun setDate(date: String)

    fun setColor(color: Int)

    fun setResult(bundle: Bundle, responseKey: String)
}