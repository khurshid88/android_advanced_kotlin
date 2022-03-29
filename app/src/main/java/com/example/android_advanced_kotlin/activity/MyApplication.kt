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
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
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