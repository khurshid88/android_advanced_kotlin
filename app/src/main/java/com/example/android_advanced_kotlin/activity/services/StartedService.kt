package com.example.android_advanced_kotlin.activity.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast

class StartedService : Service() {
    private var player: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Toast.makeText(this, "Started Service Created", Toast.LENGTH_LONG).show()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        // This will play the ringtone continuously until we stop the service.
        player!!.setLooping(true)
        // It will start the player
        player!!.start()
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stopping the player when service is destroyed
        player!!.stop()
        Toast.makeText(this, "Started Service Stopped", Toast.LENGTH_LONG).show()
    }
}
