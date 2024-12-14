package com.example.foregroundservivepracticewithnotification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.foregroundservivepracticewithnotification.App.Companion.CHANNEL_ID

class MyService : Service() {

    private val TAG = "MusicService"
    private val handler = Handler()
    private var counter = 0

    override fun onCreate() {
        super.onCreate()

    }


        private fun getCounter(): Int {
            return counter
        }

        private fun incrementCounter() {
            counter++
        }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        incrementCounter()
//        val contentIntent = Intent(this, MyBroadCastReceiver::class.java).apply {
//            putExtra("MESSAGE", "${getCounter()}")
//        }
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val contentIntent = Intent("ACTION_INCREMENT").apply {
            putExtra("MESSAGE", "${getCounter()}")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val check = intent?.getBooleanExtra("key", false)
        val input = intent?.getStringExtra("key2")
        if (check == true) {
            val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
            val notificationLayoutExpanded =
                RemoteViews(packageName, R.layout.notification_large)

            notificationLayout.setTextViewText(R.id.notification_title, input)
            notificationLayoutExpanded.setTextViewText(R.id.notification_title, input)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(0, "Increment", pendingIntent)
                .build()

            startForeground(1, builder)
        } else {
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove any callbacks to the handler when the service is destroyed

        Log.d(TAG, "Service destroyed")
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
