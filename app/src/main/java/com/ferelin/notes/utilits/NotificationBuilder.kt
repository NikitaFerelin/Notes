package com.ferelin.notes.utilits

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ferelin.notes.R

class NotificationBuilder(private val mContext: Context) {

    fun buildAndShowNotification(title: String, text: String, channelId: String, notificationId: Int) {
        val notification = NotificationCompat.Builder(mContext, channelId)
            .setSmallIcon(R.drawable.ic_note)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(mContext).notify(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNotificationChannel(id: String, name: String, description: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance).apply { this.description = description }
        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}