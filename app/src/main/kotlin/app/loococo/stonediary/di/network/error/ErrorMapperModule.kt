package app.loococo.stonediary.di.network.error

import app.loococo.domain.error.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ErrorMapperModule {

    @Provides
    @Singleton
    fun provideDefaultErrorMapper(): ErrorMapper {
        return ErrorMapper()
    }
}