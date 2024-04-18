package com.thesubgraph.wallpaper

import android.app.Application
import com.thesubgraph.wallpaper.imageLoader.cache.DiskCache
import com.thesubgraph.wallpaper.imageLoader.cache.ImageCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WallPaperApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        DiskCache(applicationContext).clear()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        DiskCache(applicationContext).clear()
    }

    override fun onTerminate() {
        super.onTerminate()
        DiskCache(applicationContext).clear()
    }
}
