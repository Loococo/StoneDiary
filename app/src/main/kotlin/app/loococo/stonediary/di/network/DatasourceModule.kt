package app.loococo.stonediary.di.network

import app.loococo.data.remote.api.AuthApi
import app.loococo.data.remote.manger.AuthDataSource
import app.loococo.domain.error.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatasourceModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(api: AuthApi, errorMapper: ErrorMapper): AuthDataSource =
        AuthDataSource(api, errorMapper)

}