package com.mintifi.ceoos.data.receiver
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
class DailyBriefingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(100, NotificationCompat.Builder(context, "briefing_channel")
            .setContentTitle("CEO OS - Good morning Pradeep!")
            .setContentText("Tap to see your daily briefing and pending tasks.")
            .setSmallIcon(android.R.drawable.ic_dialog_info).setAutoCancel(true).build())
    }
}
