package com.adr.calendar

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    private var notificationMessage = ""
    val CHANNEL_1_ID = "channel1"
    private val notificationId = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        notificationMessage = intent.extras!!.getString("eventName", "")
        notificationManager = NotificationManagerCompat.from(context)
        createNotificationChannels(context)
        notificationBuild(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "Calendar Notification Channel"
            channel1.enableLights(true)
            channel1.lightColor = Color.RED
            channel1.enableVibration(true)
            channel1.vibrationPattern =
                longArrayOf(100, 200, 300, 1000, 500, 1000, 300, 200, 1000)
            channel1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val manager = getSystemService(context,NotificationManager::class.java)
            manager?.createNotificationChannel(channel1)
        }
    }

    private fun notificationBuild(context: Context){
        val notification = NotificationCompat.Builder(context, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_date_range_black_24dp)
            .setContentTitle("Calendar Notification")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(PendingIntent.getActivity(context, 12345, Intent(context, ListRemainderActivity::class.java).putExtra("IntentListRemainder", "CloseNotification").putExtra("IntentNotificationId", notificationId), PendingIntent.FLAG_UPDATE_CURRENT))
            .build()

        notificationManager!!.notify(notificationId, notification)
    }
}