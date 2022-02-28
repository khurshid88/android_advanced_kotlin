package com.example.android_advanced_kotlin.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_advanced_kotlin.R
import com.example.android_advanced_kotlin.activity.database.UserRepository
import com.example.android_advanced_kotlin.activity.managers.RealmManager
import com.example.android_advanced_kotlin.activity.model.Post
import com.example.android_advanced_kotlin.activity.model.User
import java.util.concurrent.Executors

class DatabaseActivity : AppCompatActivity() {
    lateinit var tv_size: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        initViews()
    }

    fun initViews() {
        tv_size = findViewById<TextView>(R.id.tv_size)
        var b_realm = findViewById<Button>(R.id.b_realm)
        var b_room = findViewById<Button>(R.id.b_room)
        b_realm.setOnClickListener {
            realmDatabase()
        }
        b_room.setOnClickListener {
            roomDatabase()
        }
    }

    fun roomDatabase() {
        var repository = UserRepository(application)
        var user = User(3, "Sherzodbek")
        userExecutor(repository, user)
    }

    fun realmDatabase() {
        var post = Post(1, "PDP")
        RealmManager.instance!!.savePost(post)
        var posts = RealmManager.instance!!.loadPosts()
        tv_size.text = "Realm DB size is " + posts.size.toString()
    }

    fun userExecutor(repository: UserRepository, user: User) {
        val executor = Executors.newSingleThreadExecutor() //in background
        val handler = Handler(Looper.getMainLooper()) //in UI
        executor.execute {
            repository.saveUser(user)
            var users = repository.getUsers()
            handler.post {
                tv_size.text = "Room DB size is " + users.size.toString()
            }
        }
    }
}