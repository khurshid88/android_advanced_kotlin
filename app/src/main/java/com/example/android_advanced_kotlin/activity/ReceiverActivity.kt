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

        val tv_boot = findViewById<TextView>(R.id.tv_boot)
        val b_send = findViewById<Button>(R.id.b_send)
        b_send.setOnClickListener {

        }
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