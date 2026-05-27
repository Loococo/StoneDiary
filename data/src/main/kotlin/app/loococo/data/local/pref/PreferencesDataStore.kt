package app.loococo.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore Preferences 기반 로컬 저장소.
 * 기존 SharedPreferences("StonePreferences") 데이터를 자동 이전한다 (SharedPreferencesMigration).
 *
 * - skipLogin: 로그인 건너뛰기 플래그 (boolean)
 * - user / tokens: JSON 직렬화 문자열 (kotlinx.serialization)
 */
@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
) {

    private val dataStore: DataStore<Preferences>
        get() = context.appDataStore

    suspend fun saveSkipLogin(value: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_SKIP_LOGIN] = value }
    }

    suspend fun isSkipLogin(): Boolean =
        dataStore.data.map { it[KEY_SKIP_LOGIN] ?: false }.first()

    suspend fun saveUser(user: User) {
        dataStore.edit { prefs -> prefs[KEY_USER] = json.encodeToString(user) }
    }

    suspend fun getUser(): User? =
        dataStore.data
            .map { prefs ->
                prefs[KEY_USER]?.let { raw ->
                    runCatching { json.decodeFromString<User>(raw) }.getOrNull()
                }
            }
            .first()

    suspend fun saveTokens(tokens: Tokens) {
        dataStore.edit { prefs -> prefs[KEY_TOKENS] = json.encodeToString(tokens) }
    }

    companion object {
        internal const val DATASTORE_NAME = "stonediary_prefs"
        internal const val LEGACY_PREFERENCES_NAME = "StonePreferences"

        internal val KEY_SKIP_LOGIN = booleanPreferencesKey("is_skip_login")
        internal val KEY_USER = stringPreferencesKey("user_id")
        internal val KEY_TOKENS = stringPreferencesKey("access_token")
    }
}

/**
 * 앱 컨텍스트에 바인딩되는 DataStore 인스턴스.
 * top-level 로 선언해야 `preferencesDataStore` delegate 가 동작한다.
 */
internal val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
    name = PreferencesDataStore.DATASTORE_NAME,
    produceMigrations = { ctx ->
        listOf(SharedPreferencesMigration(ctx, PreferencesDataStore.LEGACY_PREFERENCES_NAME))
    }
)
