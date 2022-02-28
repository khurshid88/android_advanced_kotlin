package com.example.android_advanced_kotlin.activity

import android.app.Application
import android.content.res.Configuration
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.android_advanced_kotlin.activity.managers.RoomManager
import com.example.android_advanced_kotlin.activity.managers.LocaleManager
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    companion object {
        val TAG = MyApplication::class.java.simpleName
        var instance: MyApplication? = null
        var localeManager: LocaleManager? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        localeManager = LocaleManager(this)
        localeManager!!.setLocale(this)

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager!!.setLocale(this)
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.language)
    }

    val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }
}