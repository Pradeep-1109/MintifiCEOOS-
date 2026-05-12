package com.mintifi.ceoos.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.mintifi.ceoos.R
import com.mintifi.ceoos.data.service.RecordingService

class CeoOsWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_RECORD_START  = "com.mintifi.ceoos.WIDGET_RECORD_START"
        const val ACTION_RECORD_STOP   = "com.mintifi.ceoos.WIDGET_RECORD_STOP"
        const val ACTION_RECORD_PAUSE  = "com.mintifi.ceoos.WIDGET_RECORD_PAUSE"
        const val ACTION_RECORD_RESUME = "com.mintifi.ceoos.WIDGET_RECORD_RESUME"

        const val PREF_STATE    = "widget_state"
        const val STATE_IDLE    = "IDLE"
        const val STATE_REC     = "RECORDING"
        const val STATE_PAUSED  = "PAUSED"
        const val STATE_PROCESS = "PROCESSING"
        const val STATE_DONE    = "DONE"

        fun updateAllWidgets(context: Context, state: String, statusText: String = "") {
            val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString(PREF_STATE, state).putString("status_text", statusText).apply()
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(
                android.content.ComponentName(context, CeoOsWidget::class.java)
            )
            ids.forEach { id -> updateWidget(context, mgr, id, state, statusText) }
        }

        fun updateWidget(context: Context, mgr: AppWidgetManager, id: Int, state: String, statusText: String = "") {
            val views = RemoteViews(context.packageName, R.layout.widget_ceo_os)

            fun pi(action: String): PendingIntent {
                val i = Intent(context, CeoOsWidget::class.java).apply { this.action = action }
                return PendingIntent.getBroadcast(context, action.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

            when (state) {
                STATE_IDLE -> {
                    views.setTextViewText(R.id.widget_status, "Ready to record")
                    views.setTextViewText(R.id.widget_btn_primary, "⏺  Record")
                    views.setOnClickPendingIntent(R.id.widget_btn_primary, pi(ACTION_RECORD_START))
                    views.setViewVisibility(R.id.widget_btn_secondary, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_bar, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_text, android.view.View.GONE)
                }
                STATE_REC -> {
                    views.setTextViewText(R.id.widget_status, "● Recording...")
                    views.setTextViewText(R.id.widget_btn_primary, "⏸  Pause")
                    views.setTextViewText(R.id.widget_btn_secondary, "⏹  Stop")
                    views.setOnClickPendingIntent(R.id.widget_btn_primary, pi(ACTION_RECORD_PAUSE))
                    views.setOnClickPendingIntent(R.id.widget_btn_secondary, pi(ACTION_RECORD_STOP))
                    views.setViewVisibility(R.id.widget_btn_secondary, android.view.View.VISIBLE)
                    views.setViewVisibility(R.id.widget_progress_bar, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_text, android.view.View.GONE)
                }
                STATE_PAUSED -> {
                    views.setTextViewText(R.id.widget_status, "⏸ Paused")
                    views.setTextViewText(R.id.widget_btn_primary, "▶  Resume")
                    views.setTextViewText(R.id.widget_btn_secondary, "⏹  Stop")
                    views.setOnClickPendingIntent(R.id.widget_btn_primary, pi(ACTION_RECORD_RESUME))
                    views.setOnClickPendingIntent(R.id.widget_btn_secondary, pi(ACTION_RECORD_STOP))
                    views.setViewVisibility(R.id.widget_btn_secondary, android.view.View.VISIBLE)
                    views.setViewVisibility(R.id.widget_progress_bar, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_text, android.view.View.GONE)
                }
                STATE_PROCESS -> {
                    views.setTextViewText(R.id.widget_status, "Processing...")
                    views.setTextViewText(R.id.widget_btn_primary, "Processing")
                    views.setOnClickPendingIntent(R.id.widget_btn_primary, pi(ACTION_RECORD_START))
                    views.setViewVisibility(R.id.widget_btn_secondary, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_bar, android.view.View.VISIBLE)
                    views.setViewVisibility(R.id.widget_progress_text, android.view.View.VISIBLE)
                    views.setTextViewText(R.id.widget_progress_text, statusText.ifBlank { "Transcribing audio..." })
                }
                STATE_DONE -> {
                    views.setTextViewText(R.id.widget_status, "✓ Done!")
                    views.setTextViewText(R.id.widget_btn_primary, "⏺  New Recording")
                    views.setOnClickPendingIntent(R.id.widget_btn_primary, pi(ACTION_RECORD_START))
                    views.setViewVisibility(R.id.widget_btn_secondary, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_bar, android.view.View.GONE)
                    views.setViewVisibility(R.id.widget_progress_text, android.view.View.VISIBLE)
                    views.setTextViewText(R.id.widget_progress_text, statusText.ifBlank { "Tasks extracted successfully" })
                }
            }
            mgr.updateAppWidget(id, views)
        }
    }

    override fun onUpdate(context: Context, mgr: AppWidgetManager, ids: IntArray) {
        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
        val state = prefs.getString(PREF_STATE, STATE_IDLE) ?: STATE_IDLE
        val statusText = prefs.getString("status_text", "") ?: ""
        ids.forEach { id -> updateWidget(context, mgr, id, state, statusText) }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_RECORD_START -> {
                // Start recording service
                val svc = Intent(context, RecordingService::class.java).apply { action = RecordingService.ACTION_START }
                context.startForegroundService(svc)
                updateAllWidgets(context, STATE_REC)
            }
            ACTION_RECORD_PAUSE -> {
                val svc = Intent(context, RecordingService::class.java).apply { action = RecordingService.ACTION_PAUSE }
                context.startService(svc)
                updateAllWidgets(context, STATE_PAUSED)
            }
            ACTION_RECORD_RESUME -> {
                val svc = Intent(context, RecordingService::class.java).apply { action = RecordingService.ACTION_RESUME }
                context.startService(svc)
                updateAllWidgets(context, STATE_REC)
            }
            ACTION_RECORD_STOP -> {
                val svc = Intent(context, RecordingService::class.java).apply { action = RecordingService.ACTION_STOP }
                context.startService(svc)
                updateAllWidgets(context, STATE_PROCESS, "Transcribing audio...")
                // Launch app to handle processing
                val appIntent = Intent(context, Class.forName("com.mintifi.ceoos.MainActivity")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("from_widget", true)
                    putExtra("widget_stop", true)
                }
                context.startActivity(appIntent)
            }
        }
    }
}
