package app.loococo.domain.usecase

import app.loococo.domain.ext.isValidEmail
import app.loococo.domain.ext.isValidName
import app.loococo.domain.ext.isValidPassword
import app.loococo.domain.model.network.error.ErrorType
import app.loococo.domain.model.network.Resource
import app.loococo.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: RegisterRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<Unit>> {
        validateInput(email, password, name)?.let { validError ->
            return flowOf(
                Resource.Error(validError)
            )
        }
        return repository.register(email, password, name)
    }

    private fun validateInput(email: String, password: String, name: String): ErrorType.ValidationError? {
        return when {
            !email.isValidEmail() -> ErrorType.ValidationError.InvalidEmail
            !password.isValidPassword() -> ErrorType.ValidationError.InvalidPassword
            !name.isValidName() -> ErrorType.ValidationError.InvalidName
            else -> null
        }
    }

}