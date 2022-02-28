package com.example.android_advanced_kotlin.activity.network.volley

interface VolleyHandler {
    fun onSuccess(response: String?)
    fun onError(error: String?)
}