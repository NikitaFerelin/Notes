package com.ferelin.notes.ui.create

import android.content.Context
import android.text.Editable
import androidx.core.os.bundleOf
import com.ferelin.notes.R
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.notes.utilits.CoroutineContextProvider
import com.ferelin.notes.utilits.NoteColors
import com.ferelin.notes.utilits.TextTransformer
import com.ferelin.repository.db.DataManagerHelper
import com.ferelin.repository.db.response.Response
import com.ferelin.repository.model.Note
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
open class CreatePresenter constructor(
    private val mContext: Context,
    private val mDataManager: DataManagerHelper,
    private val mCoroutineProvider: CoroutineContextProvider,
) : MvpPresenter<CreateMvpView>() {

    private lateinit var mResponseKey: String

    private val mDefaultColor = NoteColors.ADAPTIVE_DEFAULT_COLOR
    private var mSelectedColor = mDefaultColor

    fun onViewPrepared(args: CreateFragmentArgs) {
        CoroutineScope(mCoroutineProvider.IO).launch {
            mDataManager.getLastNote().first().also {
                withContext(mCoroutineProvider.MAIN) {
                    recoverNote(it)
                }
            }
        }

        mResponseKey = args.responseKey

        viewState.setFocusToContentEdit()
        viewState.showKeyboard()
    }

    fun onAcceptBtnClicked(isLocked: Boolean, title: Editable, content: Editable) {
        if (!isLocked) {
            val (transformedTitle, transformedContent) = TextTransformer.transform(title.toString(), content.toString())
            val color = if (mSelectedColor == mDefaultColor) {
                ColorTransformer.fromIntToString(mContext, NoteColors.DEFAULT_COLOR)
            } else ColorTransformer.fromIntToString(mContext, mSelectedColor)

            val result = bundleOf(
                NOTE_TITLE_KEY to transformedTitle,
                NOTE_CONTENT_KEY to transformedContent,
                NOTE_COLOR_KEY to color
            )

            // Current note is accepted and will be saved -> no longer need to store in dataStore
            CoroutineScope(mCoroutineProvider.IO).launch {
                mDataManager.clearLastNote()
            }

            viewState.apply {
                setResult(result, mResponseKey)
                hideKeyboard()
                dismiss()
            }
        } else viewState.showMessage(mContext.getString(R.string.notificationEmptyNote))
    }

    fun onBottomSheetClicked(bottomSheetState: Int) {
        if (bottomSheetState != BottomSheetBehavior.STATE_EXPANDED) {
            viewState.expandBottomSheet()
        } else viewState.collapseBottomSheet()
    }

    fun onDefaultColorClicked() {
        changeColor(mDefaultColor)
        viewState.changeIconConstraintsToDefault()
    }

    fun onOrangeColorClicked() {
        changeColor(NoteColors.COLOR_ORANGE)
        viewState.changeIconConstraintsToOrange()
    }

    fun onGrayColorClicked() {
        changeColor(NoteColors.COLOR_GRAY)
        viewState.changeIconConstraintsToGray()
    }

    fun onRedColorClicked() {
        changeColor(NoteColors.COLOR_RED)
        viewState.changeIconConstraintsToRed()
    }

    fun onTextChanged(content: Editable?, title: Editable?, isAcceptBtnLocked: Boolean) {
        val contentStr = content?.toString() ?: ""
        val titleStr = title?.toString() ?: ""
        when {
            isAcceptBtnLocked && (contentStr.isNotEmpty() || titleStr.isNotEmpty()) -> viewState.unlockAcceptButton()
            !isAcceptBtnLocked && contentStr.isEmpty() && titleStr.isEmpty() -> viewState.lockAcceptButton()
        }
    }

    fun onBackBtnClicked() {
        viewState.apply {
            hideKeyboard()
            dismiss()
        }
    }

    suspend fun onSaveInstanceState(title: Editable?, content: Editable?) {
        val titleStr = title?.toString() ?: ""
        val contentStr = content?.toString() ?: ""
        val colorStr = ColorTransformer.fromIntToString(mContext, mSelectedColor)
        mDataManager.saveLastNotePreferences(titleStr, contentStr, colorStr)
    }

    private fun changeColor(color: Int) {
        if (mSelectedColor != color) {
            val settableColor = ColorTransformer.getSettableColor(mContext, color)
            viewState.setSelectedColor(settableColor)
            mSelectedColor = color
        }
    }

    private fun recoverNote(response: Response<Note>) {
        if (response is Response.Success) {
            val note = response.data
            viewState.apply {
                setNote(note.title, note.content)
                setSelectedColor(ColorTransformer.fromStringToInt(note.color))
                when (note.color) {
                    ColorTransformer.fromIntToString(mContext,
                        NoteColors.ADAPTIVE_DEFAULT_COLOR),
                    -> changeIconConstraintsToDefault()
                    ColorTransformer.fromIntToString(mContext, NoteColors.COLOR_RED) -> changeIconConstraintsToRed()
                    ColorTransformer.fromIntToString(mContext, NoteColors.COLOR_GRAY) -> changeIconConstraintsToGray()
                    ColorTransformer.fromIntToString(mContext, NoteColors.COLOR_ORANGE) -> changeIconConstraintsToOrange()
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