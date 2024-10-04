package app.loococo.stonediary.di

import app.loococo.data.repository.DiaryRepositoryImpl
import app.loococo.domain.repository.DiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideDiaryRepository(repository: DiaryRepositoryImpl): DiaryRepository
}