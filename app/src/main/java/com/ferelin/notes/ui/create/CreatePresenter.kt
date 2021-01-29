package com.ferelin.notes.ui.create

import android.content.Context
import android.text.Editable
import androidx.core.os.bundleOf
import com.ferelin.notes.base.BasePresenter
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.notes.utilits.NoteColors
import com.ferelin.notes.utilits.TextTransformer
import com.ferelin.repository.db.AppDataManager
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.model.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext

class CreatePresenter<T : CreateMvpView>(private val context: Context) : BasePresenter<T>(), CreateMvpPresenter<T> {

    private val mDataManager = AppDataManager.getInstance(context)

    private val mDefaultColor = NoteColors.ADAPTIVE_DEFAULT_COLOR
    private var mSelectedColor = mDefaultColor
    private lateinit var mResponseKey: String

    override suspend fun onViewPrepared(args: CreateFragmentArgs) {
        mResponseKey = args.responseKey

        mDataManager.getLastNote().take(1).collect {
            withContext(Dispatchers.Main) {
                recoverNote(view, it)
            }
        }
    }

    override suspend fun onAcceptBtnClicked(isLocked: Boolean, title: Editable, content: Editable) {
        if (!isLocked) {
            val (transformedTitle, transformedContent) = TextTransformer.transform(title.toString(), content.toString())
            val color = if (mSelectedColor == mDefaultColor) {
                ColorTransformer.fromIntToString(context, NoteColors.DEFAULT_COLOR)
            } else ColorTransformer.fromIntToString(context, mSelectedColor)
            val result = bundleOf(
                NOTE_CONTENT_KEY to transformedContent,
                NOTE_TITLE_KEY to transformedTitle,
                NOTE_COLOR_KEY to color
            )

            // Current note is accepted and will be saved -> no longer need to store in dataStore
            withContext(Dispatchers.IO) {
                mDataManager.clearLastNote()
            }

            view.apply {
                setResult(result, mResponseKey)
                dismiss()
            }
        } else view.showMessage("Note is empty")
    }

    override fun onBottomSheetClicked(bottomSheetState: Int) {
        if (bottomSheetState != BottomSheetBehavior.STATE_EXPANDED) {
            view.expandBottomSheet()
        } else view.collapseBottomSheet()
    }

    override fun onDefaultColorClicked() {
        changeColor(mDefaultColor)
        view.selectedColorIconToDefault()
    }

    override fun onOrangeColorClicked() {
        changeColor(NoteColors.COLOR_ORANGE)
        view.selectedColorIconToOrange()
    }

    override fun onGrayColorClicked() {
        changeColor(NoteColors.COLOR_GRAY)
        view.selectedColorIconToGray()
    }

    override fun onRedColorClicked() {
        changeColor(NoteColors.COLOR_RED)
        view.selectedColorIconToRed()
    }

    override fun onTextChanged(content: Editable?, title: Editable?, isAcceptBtnLocked: Boolean) {
        val contentStr = content?.toString() ?: ""
        val titleStr = title?.toString() ?: ""
        when {
            isAcceptBtnLocked && (contentStr.isNotEmpty() || titleStr.isNotEmpty()) -> view.unlockAcceptButton()
            !isAcceptBtnLocked && contentStr.isEmpty() && titleStr.isEmpty() -> view.lockAcceptButton()
        }
    }

    override fun onBackBtnClicked() {
        view.dismiss()
    }

    override suspend fun onSaveInstanceState(title: Editable?, content: Editable?) {
        val titleStr = title?.toString() ?: ""
        val contentStr = content?.toString() ?: ""
        val colorStr = ColorTransformer.fromIntToString(context, mSelectedColor)
        mDataManager.setLastNote(titleStr, contentStr, colorStr)
    }

    private fun changeColor(color: Int) {
        if (mSelectedColor != color) {
            val settableColor = ColorTransformer.getSettableColor(context, color)
            view.setSelectedColor(settableColor)
            mSelectedColor = color
        }
    }

    private fun recoverNote(view: CreateMvpView, response: Response<Note>) {
        if (response is Response.Success) {
            val note = response.data
            view.apply {
                setNote(note.title, note.content)
                setSelectedColor(ColorTransformer.fromStringToInt(note.color))
                when (note.color) {
                    ColorTransformer.fromIntToString(context, NoteColors.ADAPTIVE_DEFAULT_COLOR) -> selectedColorIconToDefault()
                    ColorTransformer.fromIntToString(context, NoteColors.COLOR_RED) -> selectedColorIconToRed()
                    ColorTransformer.fromIntToString(context, NoteColors.COLOR_GRAY) -> selectedColorIconToGray()
                    ColorTransformer.fromIntToString(context, NoteColors.COLOR_ORANGE) -> selectedColorIconToOrange()
                }
            }
        }
    }

    companion object {
        const val NOTE_CONTENT_KEY = "CREATE_NOTE_CONTENT_KEY_BUNDLE"
        const val NOTE_TITLE_KEY = "CREATE_NOTE_TITLE_KEY_BUNDLE"
        const val NOTE_COLOR_KEY = "CREATE_NOTE_COLOR_KEY_BUNDLE"
    }
}