package app.loococo.stonediary.di

import app.loococo.data.repository.DiaryRepositoryImpl
import app.loococo.data.repository.GalleryRepositoryImpl
import app.loococo.data.repository.ImageCropRepositoryImpl
import app.loococo.data.repository.ImageSaveRepositoryImpl
import app.loococo.domain.repository.DiaryRepository
import app.loococo.domain.repository.GalleryRepository
import app.loococo.domain.repository.ImageCropRepository
import app.loococo.domain.repository.ImageSaveRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideDiaryRepository(repository: DiaryRepositoryImpl): DiaryRepository

    @Binds
    fun provideGalleryRepository(repository: GalleryRepositoryImpl): GalleryRepository

    @Binds
    fun provideImageCropRepository(repository: ImageCropRepositoryImpl): ImageCropRepository

    @Binds
    fun provideImageSaveRepository(repository: ImageSaveRepositoryImpl): ImageSaveRepository
}