package com.thesubgraph.wallpaper.imageLoader.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DiskCache(context: Context) : ImageCache {
    private val cacheDir = context.cacheDir
    private val maxSize: Long = (Runtime.getRuntime().totalMemory() * 0.03).toLong()

    override fun get(url: String): Bitmap? {
        val file = File(cacheDir, url.hashCode().toString())
        return if (file.exists()) {
            BitmapFactory.decodeStream(FileInputStream(file))
        } else {
            null
        }

    }

    override fun put(url: String, bitmap: Bitmap) {
        val file = File(cacheDir, url.hashCode().toString())
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()


    }

    override fun clear() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }
}