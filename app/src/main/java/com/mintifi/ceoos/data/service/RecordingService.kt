package com.mintifi.ceoos.data.service
import android.app.*
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.mintifi.ceoos.MainActivity
import java.io.File

class RecordingService : Service() {
    private var recorder: MediaRecorder? = null
    companion object {
        const val ACTION_START = "START"; const val ACTION_STOP = "STOP"
        const val ACTION_PAUSE = "PAUSE"; const val ACTION_RESUME = "RESUME"
        var currentFilePath: String? = null
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) { ACTION_START -> start(); ACTION_STOP -> stop(); ACTION_PAUSE -> pause(); ACTION_RESUME -> resume() }
        return START_STICKY
    }
    private fun start() {
        val file = File(cacheDir, "rec_" + System.currentTimeMillis() + ".m4a")
        currentFilePath = file.absolutePath
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(this)
                   else @Suppress("DEPRECATION") MediaRecorder()
        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(44100); setAudioEncodingBitRate(128000)
            setOutputFile(file.absolutePath); prepare(); start()
        }
        startForeground(1, NotificationCompat.Builder(this, "recording_channel")
            .setContentTitle("CEO OS").setContentText("Recording CEO call...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now).setOngoing(true).build())
    }
    private fun stop() { runCatching { recorder?.stop() }; recorder?.release(); recorder = null; stopForeground(STOP_FOREGROUND_REMOVE); stopSelf() }
    private fun pause() { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) runCatching { recorder?.pause() } }
    private fun resume() { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) runCatching { recorder?.resume() } }
}
