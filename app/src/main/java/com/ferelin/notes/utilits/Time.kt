package com.ferelin.notes.utilits

data class Time(
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var hour: Int? = null,
    var minute: Int? = null,
) {
    override fun toString(): String {
        return if (year == null) "" else "$day-$month-$year $hour:$minute"
    }
}