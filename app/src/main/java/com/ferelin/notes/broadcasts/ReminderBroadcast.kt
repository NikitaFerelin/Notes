package com.ferelin.notes.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ferelin.notes.sources.NotificationsSource
import com.ferelin.notes.ui.create.CreatePresenter
import com.ferelin.notes.utilits.NotificationBuilder

class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.extras?.get(CreatePresenter.NOTE_TITLE_KEY).toString()
        val content = intent.extras?.get(CreatePresenter.NOTE_CONTENT_KEY).toString()
        val id = intent.extras?.get(CreatePresenter.NOTE_REQUEST_KEY) as Int

        NotificationBuilder(context).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerNotificationChannel(
                    NotificationsSource.NOTE_CHANNEL_ID,
                    context.getString(NotificationsSource.NOTE_CHANNEL_NAME),
                    context.getString(NotificationsSource.NOTE_CHANNEL_DESCRIPTION),
                    NotificationsSource.NOTE_CHANNEL_IMPORTANCE)
            }

            buildAndShowNotification(title, content, NotificationsSource.NOTE_CHANNEL_ID, id)
        }
    }
}