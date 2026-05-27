package app.loococo.stonediary

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * 앱 진입점.
 * 디버그 빌드에서만 Timber.DebugTree를 심어 릴리즈 빌드 로그 누출을 방지한다.
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
