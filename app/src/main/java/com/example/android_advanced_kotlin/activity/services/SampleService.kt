package com.example.android_advanced_kotlin.activity.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SampleService : Service() {
    var startMode // indicates how to behave if the service is killed
            = 0
    var binder // interface for clients that bind
            : IBinder? = null
    var allowRebind // indicates whether onRebind should be used
            = false

    override fun onCreate() {
        // The service is being created
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // The service is starting, due to a call to startService()
        return startMode
    }

    override fun onBind(intent: Intent): IBinder? {
        // A client is binding to the service with bindService()
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return allowRebind
    }

    override fun onRebind(intent: Intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
    }
}
