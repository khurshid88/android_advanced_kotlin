package com.example.android_advanced_kotlin.activity

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.receivers.NetworkBroadcastReceiver

class ReceiverActivity : AppCompatActivity() {
    lateinit var receiver: NetworkBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        initViews()
    }

    fun initViews() {
        receiver = NetworkBroadcastReceiver()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}