package com.thesubgraph.wallpaper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.thesubgraph.wallpaper.domain.model.Photo
import com.thesubgraph.wallpaper.imageLoader.AsyncImage
import com.thesubgraph.wallpaper.ui.theme.Grey500
import com.thesubgraph.wallpaper.ui.theme.TextStyle_Size18_Weight400
import com.thesubgraph.wallpaper.view.common.Router
import com.thesubgraph.wallpaper.view.common.ViewState
import com.thesubgraph.wallpaper.view.common.components.DefaultAppBar
import com.thesubgraph.wallpaper.view.common.components.FullPageCircularLoader
import com.thesubgraph.wallpaper.view.common.components.isScrolledToEnd
import com.thesubgraph.wallpaper.viewmodel.screens.HomeViewModel

@Composable
fun HomeScreen(route: Router, viewModel: HomeViewModel) {
    val photos = viewModel.photos.collectAsState()
    val viewState = viewModel.viewState.collectAsState()

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            DefaultAppBar(
                title = {
                    Text(
                        text = "WallWrap",
                        style = TextStyle_Size18_Weight400,
                        color = Grey500
                    )
                }
            )
        }
    ) { paddingValues ->
        ScreenContent(
            paddingValues = paddingValues,
            photos = photos,
            viewState = viewState,
            onFetchMore = {
                viewModel.getPhotos()
            })
    }
}

@Composable
private fun ScreenContent(
    paddingValues: PaddingValues,
    photos: State<List<Photo>>,
    viewState: State<ViewState<Unit>>,
    onFetchMore: () -> Unit
) {
    when (viewState.value) {
        is ViewState.Error -> {

        }

        ViewState.Loading -> {
            FullPageCircularLoader()
        }

        is ViewState.Success -> {
            PhotosView(photos, onFetchMore, paddingValues)
        }

        else -> Unit
    }
}

@Composable
private fun PhotosView(
    photos: State<List<Photo>>,
    onFetchMore: () -> Unit,
    paddingValues: PaddingValues
) {
    val listState = rememberLazyStaggeredGridState()
    val scrolledToEnd by remember { derivedStateOf { listState.isScrolledToEnd() } }

    LaunchedEffect(key1 = scrolledToEnd) {
        if (photos.value.isNotEmpty() && scrolledToEnd) {
            onFetchMore()
        }
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = listState,
        columns = StaggeredGridCells.Fixed(count = 2),
        verticalItemSpacing = 5.dp,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(start = 5.dp, end = 5.dp)
    ) {
        items(items = photos.value, key = { it.id }) {
            it.imageUrls?.let { imageUrls ->
                val color = Color(android.graphics.Color.parseColor(it.color))
                val aspectRatio = it.width.toFloat() / it.height.toFloat()
                AsyncImage(
                     url = imageUrls.thumb,
                     contentDescription = null,
                     contentScale = ContentScale.Fit,
                     modifier = Modifier
                        .aspectRatio(aspectRatio).clip(RoundedCornerShape(8.dp)),
                     placeHolder = {
                         Box(modifier = Modifier
                             .background(color = color,shape = RoundedCornerShape(8.dp))
                             .aspectRatio(aspectRatio)
                             .fillMaxSize())
                     },
                )

            }
        }
    }
}

