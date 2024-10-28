package app.loococo.stonediary.di

import app.loococo.domain.repository.DiaryRepository
import app.loococo.domain.repository.ImageRepository
import app.loococo.domain.repository.ImageSaveRepository
import app.loococo.domain.usecase.DiaryUseCase
import app.loococo.domain.usecase.ImageUseCase
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
    fun provideDiaryUseCase(
        diaryRepository: DiaryRepository,
        imageSaveRepository: ImageSaveRepository
    ): DiaryUseCase = DiaryUseCase(diaryRepository, imageSaveRepository)

    @Provides
    @Singleton
    fun provideImageUseCase(repository: ImageRepository): ImageUseCase = ImageUseCase(repository)
}
