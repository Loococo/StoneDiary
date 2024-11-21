package app.loococo.stonediary.di.network.error

import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.presentation.screen.error.DefaultErrorMessageHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ErrorHandlingModule {

    @Binds
    fun provideErrorMessageHandler(handler: DefaultErrorMessageHandler): ErrorMessageHandler
}