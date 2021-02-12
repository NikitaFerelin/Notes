package com.example.provider

import com.ferelin.repository.model.Note

class TestDataProvider {

    private val mCommonTitle = "title"
    private val mCommonContent = "content"
    private val mCommonFakeColor = "color"
    private val mCommonId = 1 // id -> first row's id in room. DO NOT CHANGE
    private val mCommonTime = "11.02 14:44"

    val defaultNote = Note(
        id = mCommonId,
        title = mCommonTitle,
        content = mCommonContent,
        color = mCommonFakeColor,
        time = mCommonTime)
}