package app.loococo.domain.error

import app.loococo.domain.model.network.error.ErrorHandler
import app.loococo.domain.model.network.error.ErrorType
import javax.inject.Inject

class ErrorMapper @Inject constructor() {
    fun map(error: ErrorHandler): ErrorType {
        return when (error.code) {
            "api.user.already_exists" -> ErrorType.AuthError.UserAlreadyExists
            "api.user.invalid_credential" -> ErrorType.AuthError.UserInvalidCredential
            else -> ErrorType.NetworkError.UnknownException
        }
    }
}