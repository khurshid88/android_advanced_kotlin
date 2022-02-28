package com.example.android_advanced_kotlin.activity.managers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_advanced_kotlin.activity.database.UserDao
import com.example.android_advanced_kotlin.activity.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class RoomManager : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: RoomManager? = null

        fun getDatabase(context: Context): RoomManager {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomManager::class.java,
                        "app_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
