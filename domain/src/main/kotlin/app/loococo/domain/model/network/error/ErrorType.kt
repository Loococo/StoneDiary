package app.loococo.domain.model.network.error

sealed class ErrorType {
    sealed class NetworkError : ErrorType() {
        data object NetworkErrorException : NetworkError()
        data object UnknownException : NetworkError()
    }

    sealed class ValidationError : ErrorType() {
        data object InvalidEmail : ValidationError()
        data object InvalidPassword : ValidationError()
        data object InvalidName : ValidationError()
    }

    sealed class AuthError : ErrorType() {
        data object UserAlreadyExists : AuthError()
        data object UserInvalidCredential : AuthError()
    }

}
