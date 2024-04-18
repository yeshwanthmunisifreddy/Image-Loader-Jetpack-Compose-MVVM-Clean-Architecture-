package com.thesubgraph.wallpaper.di

import com.thesubgraph.wallpaper.domain.repository.PhotoRepository
import com.thesubgraph.wallpaper.domain.usecase.PhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @ViewModelScoped
    @Provides
    fun providePhotoUseCase(repository: PhotoRepository): PhotoUseCase {
        return PhotoUseCase(repository)
    }
}