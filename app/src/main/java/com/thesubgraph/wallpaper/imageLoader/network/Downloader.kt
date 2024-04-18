package com.thesubgraph.wallpaper.imageLoader.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.thesubgraph.wallpaper.imageLoader.cache.DiskCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class Downloader(context: Context) {
    private val scope = CoroutineScope(
        Job() + Dispatchers.IO
    )
    private val cache = DiskCache(context)
    suspend fun execute(url : String): Bitmap? {
        return withContext(scope.coroutineContext) { getData(url) }
    }

    private suspend fun getData(urlParam: String): Bitmap? {
        // Try to get the image from the cache first
        var bitmap = cache.get(urlParam)
        if (bitmap != null) {
            Log.d("Downloader", "Image loaded from cache")
            return bitmap
        }

        // If the image is not in the cache, download it
        val url = URL(urlParam)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.doInput = true
        bitmap = BitmapFactory.decodeStream(conn.inputStream)
        conn.disconnect()
        Log.d("Downloader", "Image downloaded from network")
        // Store the downloaded image in the cache
        if (bitmap != null) {
            cache.put(urlParam, bitmap)
        }

        return bitmap
    }
}