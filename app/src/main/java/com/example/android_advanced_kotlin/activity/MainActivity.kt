package com.example.android_advanced_kotlin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.database.UserRepository
import com.example.android_advanced_kotlin.activity.model.User
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    fun initViews() {
        val b_home = findViewById<Button>(R.id.b_home)
        b_home.text = getString(R.string.app_name)
        b_home.setOnClickListener {
            callLanguageActivity()
        }
    }

    fun callNetworkActivity() {
        val intent = Intent(this, NetworkActivity::class.java)
        startActivity(intent)
    }

    fun callReceiverActivity() {
        val intent = Intent(this, ReceiverActivity::class.java)
        startActivity(intent)
    }

    fun callServiceActivity() {
        val intent = Intent(this, ServiceActivity::class.java)
        startActivity(intent)
    }


    fun callStorageActivity() {
        val intent = Intent(this, StorageActivity::class.java)
        startActivity(intent)
    }

    fun callDatabaseActivity() {
        val intent = Intent(this, DatabaseActivity::class.java)
        startActivity(intent)
    }

    fun callPreferencesActivity() {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }

    fun callLanguageActivity() {
        val intent = Intent(this, LanguageActivity::class.java)
        startActivity(intent)
    }

}