package com.ferelin.notes.ui.details

import android.content.Context
import androidx.core.os.bundleOf
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.notes.utilits.NoteColors
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class DetailsPresenter(private val mContext: Context) : MvpPresenter<DetailsMvpView>() {

    private lateinit var mResponseKey: String

    fun onViewPrepared(args: DetailsFragmentArgs) {
        mResponseKey = args.responseKey
        viewState.apply {
            setContent(args.content)
            setTitle(args.title)
            setDate(args.date)
            setColor(adaptColor(args.color))
        }
    }

    fun onDeleteClicked() {
        viewState.apply {
            setResult(bundleOf(), mResponseKey)
            dismiss()
        }
    }

    fun onBackBtnClicked() {
        viewState.dismiss()
    }

    private fun adaptColor(color: String): Int {
        return if (ColorTransformer.fromIntToString(mContext, NoteColors.DEFAULT_COLOR) == color) {
            ColorTransformer.getSettableColor(mContext, NoteColors.ADAPTIVE_DEFAULT_COLOR)
        } else ColorTransformer.fromStringToInt(color)
    }
}