package com.mintifi.ceoos.data.service

import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
            Timber.e(e, "RecordingService error")
        }
        return START_NOT_STICKY
    }

    private fun startRecording() {
        try {
            // Save to app-private recordings folder (persistent, not cache)
            val recordingsDir = File(filesDir, "recordings")
            if (!recordingsDir.exists()) recordingsDir.mkdirs()

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(recordingsDir, "CEO_OS_$timestamp.m4a")
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

            startForeground(1,
                NotificationCompat.Builder(this, "recording_channel")
                    .setContentTitle("CEO OS — Recording")
                    .setContentText("Tap to return to app")
                    .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                    .setOngoing(true).build()
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

        // Copy to Downloads folder for user access
        currentFilePath?.let { path ->
            try {
                val src = File(path)
                if (src.exists() && src.length() > 0) {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val ceoOsDir = File(downloadsDir, "CEO_OS_Recordings")
                    if (!ceoOsDir.exists()) ceoOsDir.mkdirs()
                    val dst = File(ceoOsDir, src.name)
                    src.copyTo(dst, overwrite = true)
                    Timber.d("Copied recording to Downloads: ${dst.absolutePath}")
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to copy to Downloads")
            }
        }

        try { stopForeground(STOP_FOREGROUND_REMOVE) } catch (e: Exception) { Timber.e(e) }
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
        try { recorder?.stop() } catch (e: Exception) { Timber.e(e, "stop error") }
        safeReleaseRecorder()
    }

    private fun safeReleaseRecorder() {
        try { recorder?.release() } catch (e: Exception) { Timber.e(e, "release error") }
        recorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        isRecordingActive = false
        safeStopRecorder()
    }
}
