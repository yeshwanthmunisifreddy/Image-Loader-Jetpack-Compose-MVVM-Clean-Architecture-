package com.thesubgraph.wallpaper.imageLoader.cache

import android.graphics.Bitmap

interface ImageCache {
    fun get(url : String): Bitmap?
    fun put(url : String, bitmap: Bitmap)
    fun clear()
}
