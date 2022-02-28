package com.example.android_advanced_kotlin.activity.managers

import com.example.android_advanced_kotlin.activity.model.Post
import io.realm.Realm
import io.realm.RealmResults

class RealmManager {
    val TAG = RealmManager::class.java.simpleName

    companion object {
        private var realmManager: RealmManager? = null
        private lateinit var realm: Realm
        val instance: RealmManager?
            get() {
                if (realmManager == null) {
                    realmManager = RealmManager()
                }
                return realmManager
            }
    }

    init {
        realm = Realm.getDefaultInstance()
    }

    fun savePost(post: Post) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(post)
        realm.commitTransaction()
    }

    fun loadPosts(): ArrayList<Post> {
        val posts: ArrayList<Post> = ArrayList<Post>()
        val results: RealmResults<Post> = realm.where(Post::class.java).findAll()
        for(post in results){
            posts.add(post)
        }
        return posts
    }
}