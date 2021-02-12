package com.ferelin.notes.sources

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ferelin.notes.R

object NotificationsSource {
    const val NOTE_CHANNEL_ID = "101"
    const val NOTE_CHANNEL_DESCRIPTION = R.string.descriptionChannel
    const val NOTE_CHANNEL_NAME = R.string.hintChannelName

    @RequiresApi(Build.VERSION_CODES.N)
    const val NOTE_CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

    // others channels can be described below
}