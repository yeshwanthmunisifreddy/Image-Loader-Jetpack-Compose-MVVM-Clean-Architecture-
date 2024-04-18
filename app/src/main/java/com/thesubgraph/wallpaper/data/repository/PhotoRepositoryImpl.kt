package com.thesubgraph.wallpaper.data.repository

import com.thesubgraph.wallpaper.data.common.RequestWrapper
import com.thesubgraph.wallpaper.data.common.ValueResult
import com.thesubgraph.wallpaper.data.remote.ApiService
import com.thesubgraph.wallpaper.domain.model.Photo
import com.thesubgraph.wallpaper.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val requestWrapper: RequestWrapper,
) : PhotoRepository {
    override fun getPhotos(page: Int, pageSize: Int): Flow<ValueResult<List<Photo>>> {
        return flow {
            val result = requestWrapper.execute(mapper = { photos ->
                photos.map { it.mapToDomain() } }) {
                apiService.getPhotos(page = page, perSize = pageSize)
            }
            emit(result)
        }
    }
}