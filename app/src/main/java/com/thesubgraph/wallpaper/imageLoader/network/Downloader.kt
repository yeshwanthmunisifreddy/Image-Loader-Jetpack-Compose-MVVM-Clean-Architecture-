package com.thesubgraph.wallpaper.imageLoader.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.thesubgraph.wallpaper.imageLoader.cache.DiskCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resumeWithException

class Downloader private constructor(context: Context) {
    companion object {
        @Volatile
        private var instance: Downloader? = null
        fun getInstance(context: Context): Downloader {
            return instance ?: Downloader(context).also { instance = it }
        }
    }

    private val scope = CoroutineScope(
        Job() + Dispatchers.IO
    )
    private val semaphore = Semaphore(10) // Limit to 10 parallel downloads
    private val cache = DiskCache(context)
    private val client = OkHttpClient()

    suspend fun execute(url : String): Bitmap? {
        return withContext(scope.coroutineContext) {
            val bitmap = cache.get(url)
            if (bitmap != null) {
                Log.d("Downloader", "Image loaded from cache")
                return@withContext bitmap
            }else enqueue(url)
        }
    }

    private suspend fun enqueue(url: String): Bitmap? = semaphore.withPermit {
        return withContext(scope.coroutineContext) {
            val request = Request.Builder().url(url).build()
            suspendCancellableCoroutine { continuation ->
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.body?.byteStream()?.let { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }?.let { bitmap ->
                            continuation.resumeWith(Result.success(bitmap))
                            Log.d("Downloader", "Image downloaded from network")
                            cache.put(url, bitmap) // Store the downloaded image in the cache
                        } ?: continuation.resumeWith(
                            Result.failure(
                                IOException("Failed to decode bitmap")
                            )
                        )
                    }
                })
            }
        }
    }
}