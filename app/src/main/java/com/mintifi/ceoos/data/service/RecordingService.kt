package com.mintifi.ceoos.data.service

import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import timber.log.Timber
import java.io.File

class RecordingService : Service() {

    private var recorder: MediaRecorder? = null

    companion object {
        const val ACTION_START  = "START"
        const val ACTION_STOP   = "STOP"
        const val ACTION_PAUSE  = "PAUSE"
        const val ACTION_RESUME = "RESUME"
        var currentFilePath: String? = null
        var isRecordingActive: Boolean = false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            when (intent?.action) {
                ACTION_START  -> startRecording()
                ACTION_STOP   -> stopRecording()
                ACTION_PAUSE  -> pauseRecording()
                ACTION_RESUME -> resumeRecording()
            }
        } catch (e: Exception) {
            Timber.e(e, "RecordingService error on action: ${intent?.action}")
        }
        return START_NOT_STICKY
    }

    private fun startRecording() {
        try {
            val file = File(cacheDir, "rec_" + System.currentTimeMillis() + ".m4a")
            currentFilePath = file.absolutePath
            isRecordingActive = true

            val rec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else {
                @Suppress("DEPRECATION") MediaRecorder()
            }

            rec.setAudioSource(MediaRecorder.AudioSource.MIC)
            rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            rec.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            rec.setAudioSamplingRate(16000)
            rec.setAudioEncodingBitRate(64000)
            rec.setOutputFile(file.absolutePath)
            rec.prepare()
            rec.start()
            recorder = rec

            startForeground(
                1,
                NotificationCompat.Builder(this, "recording_channel")
                    .setContentTitle("CEO OS — Recording")
                    .setContentText("Tap to return to app")
                    .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                    .setOngoing(true)
                    .build()
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to start recording")
            isRecordingActive = false
            safeReleaseRecorder()
            stopSelf()
        }
    }

    private fun stopRecording() {
        isRecordingActive = false
        safeStopRecorder()
        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } catch (e: Exception) {
            Timber.e(e, "stopForeground error")
        }
        stopSelf()
    }

    private fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try { recorder?.pause() } catch (e: Exception) { Timber.e(e, "pause error") }
        }
    }

    private fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try { recorder?.resume() } catch (e: Exception) { Timber.e(e, "resume error") }
        }
    }

    private fun safeStopRecorder() {
        try {
            recorder?.stop()
        } catch (e: Exception) {
            Timber.e(e, "recorder.stop() error — file may be incomplete")
        }
        safeReleaseRecorder()
    }

    private fun safeReleaseRecorder() {
        try {
            recorder?.release()
        } catch (e: Exception) {
            Timber.e(e, "recorder.release() error")
        }
        recorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        isRecordingActive = false
        safeStopRecorder()
    }
}
