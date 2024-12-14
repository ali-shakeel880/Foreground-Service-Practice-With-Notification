package com.example.foregroundservivepracticewithnotification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class App: Application() {
companion object{    val CHANNEL_ID = "default_channel"}
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val discriptionText = "This is a Default channel for  Default Notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = discriptionText
            }
            val notificationManager: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }


    }
}