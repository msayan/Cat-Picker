package com.hololo.catpicker.sample

import android.app.Application
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso

class CatApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val picasso = Picasso.Builder(this).memoryCache(LruCache(50 * 1024 * 1024)).build()
        Picasso.setSingletonInstance(picasso)
    }
}