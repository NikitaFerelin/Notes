package com.example.provider

import com.ferelin.repository.model.Note

class RepTestingDataProvider {

    private val mCommonTitle = "title"
    private val mCommonContent = "content"
    private val mCommonFakeColor = "color"
    private val mCommonId = 1 // id -> first row's id in room. DO NOT CHANGE

    val defaultNote = Note(
        id = mCommonId,
        title = mCommonTitle,
        content = mCommonContent,
        color = mCommonFakeColor)
}