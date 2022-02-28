package com.example.android_advanced_kotlin.activity.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_advanced_kotlin.activity.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getUsers(): List<User>
}