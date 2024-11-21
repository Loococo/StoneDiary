package app.loococo.domain.usecase

import app.loococo.domain.model.LoginData
import app.loococo.domain.model.User
import app.loococo.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(private val preferencesRepository: PreferencesRepository) {

    fun saveSkipLoginState() {
        preferencesRepository.saveSkipLoginState()
    }

    fun isSkipLogin(): Boolean = preferencesRepository.isSkipLogin()

    fun saveLoginData(loginData: LoginData) {
        preferencesRepository.saveUser(loginData.user)
        preferencesRepository.saveTokens(loginData.tokens)
    }

    fun getUserInfo(): User? = preferencesRepository.userInfo()

    fun shouldShowHomeScreen(): Boolean {
        return isSkipLogin() || getUserInfo() != null
    }
}