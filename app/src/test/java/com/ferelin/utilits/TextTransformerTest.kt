package com.ferelin.utilits

import com.ferelin.notes.utilits.TextTransformer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TextTransformerTest {

    // Long content -> contentValue.length > 10
    // Empty title -> titleValue.trim().isEmpty()
    // Short title -> titleValue.length <= 15

    @Test
    fun testWithEmptyTitleAndLongContent() {
        val emptyTitle = ""
        val longContent = "Some content, more content, more content"
        val correctTransform = arrayOf("Some conte...", "Some content, more content, more content")
        Assert.assertArrayEquals(correctTransform, TextTransformer.transform(emptyTitle, longContent))
    }

    @Test
    fun testWithEmptyTitleAndShortContent() {
        val emptyTitle = ""
        val shortContent = "shortCnt"
        val correctTransform = arrayOf("shortCnt", "shortCnt")
        Assert.assertArrayEquals(correctTransform, TextTransformer.transform(emptyTitle, shortContent))
    }

    @Test
    fun testWithShortTitleAndLongContent() {
        val shortTitle = "Short title"
        val longContent = "Some content"
        val correctTransform = arrayOf("Short title", "Some content")
        Assert.assertArrayEquals(correctTransform, TextTransformer.transform(shortTitle, longContent))
    }

    @Test
    fun testWithLongTitleAndLongContent() {
        val longTitle = "This is long title"
        val longContent = "Some content"
        val correctTransform = arrayOf("This is lo...", "This is long title. Some content")
        Assert.assertArrayEquals(correctTransform, TextTransformer.transform(longTitle, longContent))
    }
}