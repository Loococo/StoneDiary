package app.loococo.data.repository

import app.loococo.data.local.pref.PreferencesDataStore
import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User
import app.loococo.domain.repository.IPreferencesRepository
import javax.inject.Inject

/**
 * DataStore Preferences 기반 구현체.
 * 기존 SharedPreferences 데이터는 PreferencesDataStore 의 마이그레이션에서 자동 이전됨.
 */
class PreferencesRepositoryImpl @Inject constructor(
    private val store: PreferencesDataStore
) : IPreferencesRepository {

    override suspend fun saveSkipLoginState() {
        store.saveSkipLogin(true)
    }

    override suspend fun isSkipLogin(): Boolean = store.isSkipLogin()

    override suspend fun saveUser(user: User) {
        store.saveUser(user)
    }

    override suspend fun userInfo(): User? = store.getUser()

    override suspend fun saveTokens(tokens: Tokens) {
        store.saveTokens(tokens)
    }
}
