package com.thesubgraph.wallpaper.domain.repository

import com.thesubgraph.wallpaper.data.common.ValueResult
import com.thesubgraph.wallpaper.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotos(page: Int, pageSize: Int): Flow<ValueResult<List<Photo>>>
}