package app.loococo.domain.repository

import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User

interface IPreferencesRepository {
    fun saveSkipLoginState()
    fun isSkipLogin(): Boolean

    fun saveUser(user: User)
    fun userInfo(): User?

    fun saveTokens(tokens: Tokens)
}