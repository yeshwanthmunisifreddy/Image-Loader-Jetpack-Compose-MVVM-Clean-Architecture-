package com.thesubgraph.wallpaper.di

import android.content.Context
import com.thesubgraph.wallpaper.data.common.RequestWrapper
import com.thesubgraph.wallpaper.data.remote.ApiService
import com.thesubgraph.wallpaper.data.repository.PhotoRepositoryImpl
import com.thesubgraph.wallpaper.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePhotoRepository(
        apiService: ApiService,
        requestWrapper: RequestWrapper,
    ): PhotoRepository{
        return PhotoRepositoryImpl(apiService = apiService, requestWrapper = requestWrapper)
    }
}
