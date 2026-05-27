package app.loococo.domain.usecase

import app.loococo.domain.model.LoginData
import app.loococo.domain.model.User
import app.loococo.domain.repository.IPreferencesRepository
import javax.inject.Inject

/**
 * 로컬 설정·인증 정보 UseCase.
 * DataStore 기반(Phase 4 마이그레이션)이므로 모든 호출이 suspend 다.
 */
class PreferencesUseCase @Inject constructor(
    private val preferencesRepository: IPreferencesRepository
) {

    suspend fun saveSkipLoginState() {
        preferencesRepository.saveSkipLoginState()
    }

    suspend fun isSkipLogin(): Boolean = preferencesRepository.isSkipLogin()

    suspend fun saveLoginData(loginData: LoginData) {
        preferencesRepository.saveUser(loginData.user)
        preferencesRepository.saveTokens(loginData.tokens)
    }

    suspend fun getUserInfo(): User? = preferencesRepository.userInfo()

    suspend fun shouldShowHomeScreen(): Boolean {
        return isSkipLogin() || getUserInfo() != null
    }
}
