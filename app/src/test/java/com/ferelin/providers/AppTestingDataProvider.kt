package com.ferelin.providers

import android.content.Context
import android.text.Editable
import androidx.core.os.bundleOf
import com.ferelin.notes.R
import com.ferelin.notes.ui.create.CreateFragmentArgs
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.ui.details.DetailsFragmentArgs
import com.ferelin.notes.utilits.ColorTransformer
import com.ferelin.notes.utilits.NoteColors
import com.ferelin.repository.model.Note

class AppTestingDataProvider(context: Context) {

    private val mCommonTitle = "TestTitle"
    private val mCommonContent = "TestContent"

    val defaultNote = Note(
        title = mCommonTitle,
        content = mCommonContent,
        color = ColorTransformer.fromIntToString(context, NoteColors.COLOR_RED))

    val defaultNoteBundle = bundleOf(
        CreatePresenter.NOTE_TITLE_KEY to defaultNote.title,
        CreatePresenter.NOTE_CONTENT_KEY to defaultNote.content,
        CreatePresenter.NOTE_COLOR_KEY to defaultNote.color
    )

    val defaultColor = "#f7f7f7"

    val defaultNotesList = listOf(
        Note(title = "title1", content = "content1", date = "date1", color = "color1"),
        Note(title = "title2", content = "content2", date = "date2", color = "color1"),
        Note(title = "title2", content = "content3", date = "date3", color = "color1"),
        Note(title = "title3", content = "content3", date = "date4", color = "color1"),
        Note(title = "title4", content = "content4", date = "date4", color = "color1"),
        Note(title = "title5", content = "content5", date = "date5", color = "color1"),
    )

    val titleQuery = defaultNotesList[1].title
    val titleQueryExpected = defaultNotesList.count { it.title.contains(titleQuery) }

    val editableTitle: Editable = Editable.Factory.getInstance().newEditable(mCommonTitle)
    val editableContent: Editable = Editable.Factory.getInstance().newEditable(mCommonContent)

    val defaultResponseKey = "TestResponseKey"

    val noteIsEmptyMessage = context.resources.getString(R.string.notificationEmptyNote)

    val createFragmentArgs = CreateFragmentArgs(defaultResponseKey)
    val detailsFragmentArgs = DetailsFragmentArgs(defaultResponseKey, defaultNote.title, defaultNote.content,
        defaultNote.date, defaultNote.color, "transitionName")
}