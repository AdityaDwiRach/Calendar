package com.adr.calendar

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder

/**
 * Created by andrew on 1/21/14.
 */
class NotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent1 = Intent(this.applicationContext, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent1, 0)

        val mNotify = Notification.Builder(this)
            .setContentTitle("Log Steps!")
            .setContentText("Log your steps for today")
            .setSmallIcon(R.drawable.ic_date_range_black_24dp)
            .setContentIntent(pIntent)
            .setSound(sound)
            .addAction(0, "Load Website", pIntent)
            .build()

        notificationManager.notify(1, mNotify)
    }
}