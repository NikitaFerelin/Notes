package com.ferelin.notes.utilits

import com.ferelin.notes.R

object TextTransformer {

    fun transform(title: String, content: String): Array<String> {
        var tempTitle = title
        var tempContent = content

        when {
            tempTitle.trim().isEmpty() -> {
                tempTitle = if (tempContent.length > 10) "${tempContent.substring(0, 10)}..." else tempContent
            }
            tempTitle.length > 15 -> {
                tempContent = "$tempTitle. $tempContent"
                tempTitle = "${tempContent.substring(0, 10)}..."
            }
            tempTitle.trim().isEmpty() && tempContent.trim().isEmpty() -> tempContent = R.string.app_name.toString()
        }

        return arrayOf(tempTitle, tempContent)
    }
}