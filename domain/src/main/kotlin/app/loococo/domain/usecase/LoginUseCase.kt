package app.loococo.domain.usecase

import app.loococo.domain.ext.isValidEmail
import app.loococo.domain.ext.isValidPassword
import app.loococo.domain.model.LoginData
import app.loococo.domain.model.network.error.ErrorType
import app.loococo.domain.model.network.Resource
import app.loococo.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<LoginData>> {
        validateInput(email, password)?.let { validError ->
            return flowOf(
                Resource.Error(validError)
            )
        }
        return loginRepository.login(email, password)
    }

    private fun validateInput(email: String, password: String): ErrorType.ValidationError? {
        return when {
            !email.isValidEmail() -> ErrorType.ValidationError.InvalidEmail
            !password.isValidPassword() -> ErrorType.ValidationError.InvalidPassword
            else -> null
        }
    }
}