package com.thesubgraph.wallpaper.domain.usecase

import com.thesubgraph.wallpaper.data.common.ValueResult
import com.thesubgraph.wallpaper.domain.model.Photo
import com.thesubgraph.wallpaper.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow

class PhotoUseCase(
    private val photoRepository: PhotoRepository
) {
    fun execute(page: Int,pageSize:Int): Flow<ValueResult<List<Photo>>> {
        return photoRepository.getPhotos(page,pageSize)
    }
}