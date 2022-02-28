package com.example.android_advanced_kotlin.activity.managers

import android.content.Context
import android.content.SharedPreferences

class PrefsManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences?

    companion object {
        private var prefsManager: PrefsManager? = null
        fun getInstance(context: Context): PrefsManager? {
            if (prefsManager == null) {
                prefsManager = PrefsManager(context)
            }
            return prefsManager
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }

    fun saveData(key: String?, value: String?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getData(key: String?): String? {
        return if (sharedPreferences != null) sharedPreferences.getString(key, "") else "en"
    }

    fun clearAll() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }
}