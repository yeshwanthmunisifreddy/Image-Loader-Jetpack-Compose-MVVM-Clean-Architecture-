package com.thesubgraph.wallpaper.imageLoader

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.thesubgraph.wallpaper.imageLoader.network.Downloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

sealed class ImageState {
    data object Loading : ImageState()
    data class Success(val bitmap: Bitmap) : ImageState()
    data class Error(val message: String) : ImageState()
}

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    placeHolder: @Composable () -> Unit = {},
    error: @Composable () -> Unit = {},
) {
    val context  = LocalContext.current
    val downloader = remember { Downloader.getInstance(context) }
    var state by remember { mutableStateOf<ImageState>(ImageState.Loading) }
    val scope = rememberCoroutineScope()
    var job:Job?= null
    LaunchedEffect(url) {
         job = scope.launch {
            state = try {
                val bitmap = withContext(Dispatchers.Main.immediate) {
                    downloader.execute(url)
                }
                if (bitmap != null) {
                    ImageState.Success(bitmap)
                } else {
                    ImageState.Error("Failed to load image")
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    Log.d("AsyncImage", "Coroutine was cancelled")
                    return@launch
                }
                ImageState.Error(e.message ?: "Unknown error")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            job?.cancel()
        }
    }
    when (val currentState = state) {
        is ImageState.Loading -> placeHolder()
        is ImageState.Success -> Image(
            bitmap = currentState.bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )

        is ImageState.Error -> error()
    }
}