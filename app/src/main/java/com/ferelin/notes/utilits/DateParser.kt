package com.ferelin.notes.utilits

import java.text.SimpleDateFormat
import java.util.*

class DateParser {

    private val mDatePattern = "d-M-yyyy hh:mm"

    fun parseStringToDate(time: String): Date {
        val formatter = SimpleDateFormat(mDatePattern, Locale.ROOT)
        return formatter.parse(time)!!
    }

    fun parseStringToMillis(time: String): Long {
        return parseStringToDate(time).time
    }
}