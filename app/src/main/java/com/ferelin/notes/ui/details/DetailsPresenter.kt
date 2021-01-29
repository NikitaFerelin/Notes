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
        val title = args.title
        val content = args.content
        val color = args.color
        val date = args.date
        view.apply {
            setContent(content)
            setTitle(title)
            setDate(date)
            if (ColorTransformer.fromIntToString(context, NoteColors.DEFAULT_COLOR) == color) {
                setColor(ColorTransformer.getSettableColor(context, NoteColors.ADAPTIVE_DEFAULT_COLOR))
            } else setColor(ColorTransformer.fromStringToInt(color))
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
}