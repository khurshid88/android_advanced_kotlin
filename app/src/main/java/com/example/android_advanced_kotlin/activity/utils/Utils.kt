package com.example.android_advanced_kotlin.activity.utils

import android.content.Context
import android.widget.Toast

class Utils {

    companion object {
        fun fireToast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}