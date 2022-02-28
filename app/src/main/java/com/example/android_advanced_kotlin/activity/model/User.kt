package com.example.android_advanced_kotlin.activity.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
open class User(@PrimaryKey val id: Int, @ColumnInfo(name = "full_name") val fullname: String)