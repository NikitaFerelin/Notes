package com.ferelin.notes.utilits

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object AlarmSetter {

    fun set(context: Context, date: String, alarmIntent: Intent, requestCode: Int) {
        val time = DateParser().parseStringToMillis(date)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, 0)
        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, time, time, pendingIntent)
    }
}