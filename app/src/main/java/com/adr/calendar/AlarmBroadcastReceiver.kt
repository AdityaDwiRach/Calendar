package com.adr.calendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    val CHANNEL_1_ID = "channel1"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        notificationManager = NotificationManagerCompat.from(context)
        createNotificationChannels(context)
        notificationBuild(context)
        Toast.makeText(context, "Alarm Started", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is Channel 1"
            channel1.enableLights(true)
            channel1.lightColor = Color.RED
            channel1.enableVibration(true)
            channel1.vibrationPattern =
                longArrayOf(100, 200, 300, 1000, 500, 1000, 300, 200, 1000)

            val manager = getSystemService(context,NotificationManager::class.java)
            manager?.createNotificationChannel(channel1)
        }
    }

    private fun notificationBuild(context: Context){
        val notification = NotificationCompat.Builder(context, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Testiiing notifikasi")
            .setContentText("LED Test")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager!!.notify(1, notification)
    }
}