package com.example.android_advanced_kotlin.activity.database

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.android_advanced_kotlin.activity.managers.RoomManager
import com.example.android_advanced_kotlin.activity.model.User
import java.util.concurrent.Executors

class UserRepository {
    lateinit var userDao: UserDao

    constructor(application: Application) {
        val db = RoomManager.getDatabase(application)
        userDao = db.userDao()
    }

    fun getUsers(): List<User> {
        return userDao.getUsers()
    }

    fun saveUser(user: User) {
        userDao.saveUser(user)
    }
}