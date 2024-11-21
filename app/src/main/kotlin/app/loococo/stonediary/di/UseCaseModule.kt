package app.loococo.stonediary.di

import app.loococo.domain.repository.DiaryRepository
import app.loococo.domain.repository.GalleryRepository
import app.loococo.domain.repository.ImageCropRepository
import app.loococo.domain.repository.ImageSaveRepository
import app.loococo.domain.repository.LoginRepository
import app.loococo.domain.repository.PreferencesRepository
import app.loococo.domain.repository.RegisterRepository
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.domain.usecase.GalleryUseCase
import app.loococo.domain.usecase.ImageCalculateUesCase
import app.loococo.domain.usecase.ImageCropUseCase
import app.loococo.domain.usecase.ImageSaveUseCase
import app.loococo.domain.usecase.LoginUseCase
import app.loococo.domain.usecase.PreferencesUseCase
import app.loococo.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideDiaryUseCase(repository: DiaryRepository): DiaryUseCase = DiaryUseCase(repository)

    @Provides
    @Singleton
    fun provideGalleryUseCase(repository: GalleryRepository): GalleryUseCase =
        GalleryUseCase(repository)

    @Provides
    @Singleton
    fun provideImageCropUseCase(repository: ImageCropRepository): ImageCropUseCase =
        ImageCropUseCase(repository)

    @Provides
    @Singleton
    fun provideImageSaveUseCase(repository: ImageSaveRepository): ImageSaveUseCase =
        ImageSaveUseCase(repository)

    @Provides
    @Singleton
    fun provideImageCalculateUesCase(): ImageCalculateUesCase = ImageCalculateUesCase()

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: LoginRepository): LoginUseCase = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: RegisterRepository): RegisterUseCase = RegisterUseCase(repository)

    @Provides
    @Singleton
    fun providePreferencesUseCase(repository: PreferencesRepository): PreferencesUseCase = PreferencesUseCase(repository)

}
