package com.example.android_advanced_kotlin.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.managers.LocaleManager
import java.util.*

class LanguageActivity: AppCompatActivity(){

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        initViews()
    }

    fun initViews() {
        context = this
        val b_english = findViewById<Button>(R.id.b_english)
        b_english.setOnClickListener {
            //setLocale("en");
            //MyApplication.localeManager!!.setNewLocale(context, LocaleManager.LANGUAGE_ENGLISH)
            finish()
        }
        val b_russian = findViewById<Button>(R.id.b_russian)
        b_russian.setOnClickListener {
            //setLocale("ru")
            //MyApplication.localeManager!!.setNewLocale(context, LocaleManager.LANGUAGE_RUSSIAN)
            finish()
        }
        val b_uzbek = findViewById<Button>(R.id.b_uzbek)
        b_uzbek.setOnClickListener {
            //setLocale("uz")
            //MyApplication.localeManager!!.setNewLocale(context, LocaleManager.LANGUAGE_UZBEK)
            finish()
        }

        // one = 1
        val one = resources.getQuantityString(R.plurals.my_cats, 1, 1)
        // few = 2~4
        val few = resources.getQuantityString(R.plurals.my_cats, 2, 3)
        // many = 5~
        val many = resources.getQuantityString(R.plurals.my_cats, 5, 10)

        Log.d("@@@one ", one)
        Log.d("@@@few ", few)
        Log.d("@@@many ", many)
    }

    fun setLocale(language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,
            baseContext.resources.displayMetrics)
        finish()
    }

}