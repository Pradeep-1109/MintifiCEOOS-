package com.mintifi.ceoos
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import timber.log.Timber
class MintifiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(NotificationChannel("recording_channel", "Recording", NotificationManager.IMPORTANCE_LOW))
            nm.createNotificationChannel(NotificationChannel("briefing_channel", "Daily Briefing", NotificationManager.IMPORTANCE_HIGH))
        }
    }
}
