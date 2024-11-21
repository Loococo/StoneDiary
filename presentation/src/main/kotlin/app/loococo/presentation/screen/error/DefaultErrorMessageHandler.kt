package app.loococo.presentation.screen.error

import android.content.Context
import app.loococo.domain.error.ErrorMessageHandler
import app.loococo.domain.model.network.error.ErrorType
import app.loococo.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultErrorMessageHandler @Inject constructor(
    @ApplicationContext private val context: Context
) : ErrorMessageHandler {

    override fun getErrorMessage(error: ErrorType): String = when (error) {
        is ErrorType.ValidationError -> getValidationErrorMessage(error)
        is ErrorType.AuthError -> getAuthErrorMessage(error)
        is ErrorType.NetworkError -> getNetworkErrorMessage(error)
    }

    private fun getValidationErrorMessage(error: ErrorType.ValidationError): String = when (error) {
        is ErrorType.ValidationError.InvalidEmail -> getString(R.string.invalid_email)
        is ErrorType.ValidationError.InvalidPassword -> getString(R.string.invalid_password)
        is ErrorType.ValidationError.InvalidName -> getString(R.string.invalid_name)
    }

    private fun getAuthErrorMessage(error: ErrorType.AuthError): String = when (error) {
        is ErrorType.AuthError.UserAlreadyExists -> getString(R.string.user_exists)
        ErrorType.AuthError.UserInvalidCredential -> getString(R.string.user_invalid_credential)
    }

    private fun getNetworkErrorMessage(error: ErrorType.NetworkError): String = when (error) {
        is ErrorType.NetworkError.NetworkErrorException -> getString(R.string.no_internet)
        ErrorType.NetworkError.UnknownException -> getString(R.string.unknown_exception)
    }

    private fun getString(resId: Int): String = context.getString(resId)
}