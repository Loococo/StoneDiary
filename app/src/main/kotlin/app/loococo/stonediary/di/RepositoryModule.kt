package app.loococo.stonediary.di

import app.loococo.data.repository.DiaryRepositoryImpl
import app.loococo.data.repository.GalleryRepositoryImpl
import app.loococo.data.repository.ImageCropRepositoryImpl
import app.loococo.data.repository.ImageSaveRepositoryImpl
import app.loococo.data.repository.LoginRepositoryImpl
import app.loococo.data.repository.PreferencesRepositoryImpl
import app.loococo.data.repository.RegisterRepositoryImpl
import app.loococo.domain.repository.IDiaryRepository
import app.loococo.domain.repository.IGalleryRepository
import app.loococo.domain.repository.IImageCropRepository
import app.loococo.domain.repository.IImageSaveRepository
import app.loococo.domain.repository.ILoginRepository
import app.loococo.domain.repository.IPreferencesRepository
import app.loococo.domain.repository.IRegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideDiaryRepository(repository: DiaryRepositoryImpl): IDiaryRepository

    @Binds
    fun provideGalleryRepository(repository: GalleryRepositoryImpl): IGalleryRepository

    @Binds
    fun provideImageCropRepository(repository: ImageCropRepositoryImpl): IImageCropRepository

    @Binds
    fun provideImageSaveRepository(repository: ImageSaveRepositoryImpl): IImageSaveRepository

    @Binds
    fun provideLoginRepository(repository: LoginRepositoryImpl): ILoginRepository

    @Binds
    fun provideRegisterRepository(repository: RegisterRepositoryImpl): IRegisterRepository

    @Binds
    fun provideRPreferencesRepository(repository: PreferencesRepositoryImpl): IPreferencesRepository
}