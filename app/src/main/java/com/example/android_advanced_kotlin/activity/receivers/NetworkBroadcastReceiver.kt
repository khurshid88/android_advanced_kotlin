package com.example.android_advanced_kotlin.activity.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

class NetworkBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            Toast.makeText(context, "Connectivity changed", Toast.LENGTH_SHORT).show()
        }
    }
}