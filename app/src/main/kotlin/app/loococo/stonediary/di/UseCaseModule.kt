package app.loococo.stonediary.di

import app.loococo.domain.repository.DiaryRepository
import app.loococo.domain.usecase.DiaryUseCase
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
}
