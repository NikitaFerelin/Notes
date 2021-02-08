package com.ferelin.utilits

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.ferelin.notes.utilits.ColorTransformer
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ColorTransformerTest {

    private val mTestColorResource = android.R.color.black
    private val mTestColorCode = "#000000"
    private val mContext = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testColorTransformingFromIntToString() {
        assertEquals(mTestColorCode, ColorTransformer.fromIntToString(mContext, mTestColorResource))
    }
}