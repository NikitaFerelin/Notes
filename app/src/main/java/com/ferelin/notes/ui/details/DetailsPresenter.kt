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
        val note = args.note
        viewState.apply {
            setContent(note.content)
            setTitle(note.title)
            setDate(note.date)
            setColor(adaptColor(note.color))

            if (note.time.isEmpty()) {
                hideReminder()
            } else setReminder(note.time)
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