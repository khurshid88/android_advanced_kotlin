package com.example.android_advanced_kotlin.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.services.BoundService
import com.example.android_advanced_kotlin.activity.services.StartedService

class ServiceActivity : AppCompatActivity() {
    var boundService: BoundService? = null
    var isBound = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        initViews()
    }

    fun initViews() {
        val b_start1 = findViewById<Button>(R.id.b_start1)
        val b_stop1 = findViewById<Button>(R.id.b_stop1)
        val b_start2 = findViewById<Button>(R.id.b_start2)
        val b_stop2 = findViewById<Button>(R.id.b_stop2)
        val b_timestamp = findViewById<Button>(R.id.b_timestamp)
        val tv_timestamp = findViewById<TextView>(R.id.tv_timestamp)
        b_start1.setOnClickListener { startStartedService() }
        b_stop1.setOnClickListener { stopStartedService() }
        b_start2.setOnClickListener { startBoundService() }
        b_stop2.setOnClickListener { stopBoundService() }
        b_timestamp.setOnClickListener {
            if (isBound) {
                tv_timestamp.setText(boundService!!.timestamp)
            }
        }
    }

    fun startStartedService() {
        startService(Intent(this, StartedService::class.java))
    }

    fun stopStartedService() {
        stopService(Intent(this, StartedService::class.java))
    }

    fun startBoundService() {
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    fun stopBoundService() {
        if (isBound) {
            unbindService(mServiceConnection)
            isBound = false
        }
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder: BoundService.MyBinder = service as BoundService.MyBinder
            boundService = myBinder.getService()
            isBound = true
        }
    }
}