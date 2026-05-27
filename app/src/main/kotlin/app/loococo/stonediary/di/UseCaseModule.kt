package app.loococo.stonediary.di

import app.loococo.domain.repository.IDiaryRepository
import app.loococo.domain.repository.IGalleryRepository
import app.loococo.domain.repository.IImageCropRepository
import app.loococo.domain.repository.IImageSaveRepository
import app.loococo.domain.repository.ILoginRepository
import app.loococo.domain.repository.IPreferencesRepository
import app.loococo.domain.repository.IRegisterRepository
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
    fun provideDiaryUseCase(repository: IDiaryRepository): DiaryUseCase = DiaryUseCase(repository)

    @Provides
    @Singleton
    fun provideGalleryUseCase(repository: IGalleryRepository): GalleryUseCase =
        GalleryUseCase(repository)

    @Provides
    @Singleton
    fun provideImageCropUseCase(repository: IImageCropRepository): ImageCropUseCase =
        ImageCropUseCase(repository)

    @Provides
    @Singleton
    fun provideImageSaveUseCase(repository: IImageSaveRepository): ImageSaveUseCase =
        ImageSaveUseCase(repository)

    @Provides
    @Singleton
    fun provideImageCalculateUesCase(): ImageCalculateUesCase = ImageCalculateUesCase()

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: ILoginRepository): LoginUseCase = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: IRegisterRepository): RegisterUseCase = RegisterUseCase(repository)

    @Provides
    @Singleton
    fun providePreferencesUseCase(repository: IPreferencesRepository): PreferencesUseCase = PreferencesUseCase(repository)

}
