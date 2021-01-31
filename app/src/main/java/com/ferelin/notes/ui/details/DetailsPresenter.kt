package com.ferelin.notes.ui.details

import android.content.Context
import androidx.core.os.bundleOf
import com.ferelin.notes.base.BasePresenter
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.notes.utilits.NoteColors

class DetailsPresenter<T : DetailsMvpView>(private val context: Context) : BasePresenter<T>(), DetailsMvpPresenter<T> {

    private lateinit var mResponseKey: String

    override fun onViewPrepared(args: DetailsFragmentArgs) {
        mResponseKey = args.responseKey
        view.apply {
            setContent(args.content)
            setTitle(args.title)
            setDate(args.date)
            setColor(adaptColor(args.color))
        }
    }

    override fun onDeleteClicked() {
        view.apply {
            setResult(bundleOf(), mResponseKey)
            dismiss()
        }
    }

    override fun onBackBtnClicked() {
        view.dismiss()
    }

    private fun adaptColor(color: String): Int {
        return if (ColorTransformer.fromIntToString(context, NoteColors.DEFAULT_COLOR) == color) {
            ColorTransformer.getSettableColor(context, NoteColors.ADAPTIVE_DEFAULT_COLOR)
        } else ColorTransformer.fromStringToInt(color)
    }
}