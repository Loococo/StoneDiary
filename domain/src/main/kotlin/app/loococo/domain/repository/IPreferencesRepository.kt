package app.loococo.domain.repository

import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User

/**
 * 로컬 설정·인증 상태 저장소.
 * 구현체는 :data 의 DataStore Preferences (Phase 4 마이그레이션).
 */
interface IPreferencesRepository {
    suspend fun saveSkipLoginState()
    suspend fun isSkipLogin(): Boolean

    suspend fun saveUser(user: User)
    suspend fun userInfo(): User?

    suspend fun saveTokens(tokens: Tokens)
}
