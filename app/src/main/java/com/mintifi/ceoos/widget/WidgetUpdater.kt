package com.mintifi.ceoos.widget

import android.content.Context

/**
 * Helper called from MainViewModel to keep widget in sync
 * with recording and processing state.
 */
object WidgetUpdater {

    fun onRecordingStarted(context: Context) {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_REC)
    }

    fun onRecordingPaused(context: Context) {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_PAUSED)
    }

    fun onRecordingResumed(context: Context) {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_REC)
    }

    fun onProcessingStarted(context: Context, step: String = "Transcribing audio...") {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_PROCESS, step)
    }

    fun onProcessingStep(context: Context, step: String) {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_PROCESS, step)
    }

    fun onProcessingDone(context: Context, result: String = "Tasks extracted successfully") {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_DONE, result)
    }

    fun onReset(context: Context) {
        CeoOsWidget.updateAllWidgets(context, CeoOsWidget.STATE_IDLE)
    }
}
