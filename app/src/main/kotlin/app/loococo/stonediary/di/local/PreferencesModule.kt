package app.loococo.stonediary.di.local

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Preferences DI 모듈.
 * 실제 DataStore 는 @Singleton @Inject 로 자동 생성되므로,
 * 여기서는 직렬화에 사용할 Json 인스턴스만 제공한다.
 */
@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
}
