package app.loococo.data.repository

import app.loococo.data.local.pref.SharedPreferencesManager
import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User
import app.loococo.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val pref: SharedPreferencesManager
) : PreferencesRepository {

    companion object {
        private const val KEY_SKIP_LOGIN = "is_skip_login"
        private const val KEY_USER = "user_id"
        private const val KEY_TOKEN = "access_token"
    }


    override fun saveSkipLoginState() {
        pref.saveBoolean(KEY_SKIP_LOGIN, true)
    }

    override fun isSkipLogin(): Boolean {
        return pref.getBoolean(KEY_SKIP_LOGIN)
    }

    override fun saveUser(user: User) {
        pref.saveObject(KEY_USER, user)
    }

    override fun userInfo(): User? {
        return pref.getObject(KEY_USER, User::class.java)
    }

    override fun saveTokens(tokens: Tokens) {
        pref.saveObject(KEY_TOKEN, tokens)
    }
}