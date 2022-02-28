package com.example.android_advanced_kotlin.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.managers.PrefsManager

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        initViews()
    }

    fun initViews() {
        val et_email = findViewById<EditText>(R.id.et_email)
        val b_save = findViewById<Button>(R.id.b_save)
        b_save.setOnClickListener {
            val email = et_email.text.toString()
            PrefsManager.getInstance(this)!!.saveData("email",email)
            var data = PrefsManager.getInstance(this)!!.getData("email")

        }
    }

    fun saveEmail(email: String?) {
        val prefs = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun loadEmail(): String? {
        val prefs = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        return prefs.getString("email", "pdp@gmail.com")
    }

    fun removeEmail() {
        val prefs = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("email")
        editor.apply()
    }

    fun clearAll() {
        val prefs = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

}