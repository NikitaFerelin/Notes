package com.ferelin.notes.utilits

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object ColorTransformer {

    fun getSettableColor(context: Context, @ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun fromStringToInt(color: String): Int {
        return Color.parseColor(color)
    }

    fun fromIntToString(context: Context, @ColorRes color: Int): String {
        val unformattedColor = context.resources.getString(0 + color)
        return "#${unformattedColor.substring(3)}"
    }
}