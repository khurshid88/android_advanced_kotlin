package com.example.android_advanced_kotlin.activity.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Post : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var caption: String? = null

    constructor() {}
    constructor(id: Long, caption: String?) {
        this.id = id
        this.caption = caption
    }
}