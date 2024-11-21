package app.loococo.domain.error

import app.loococo.domain.model.network.error.ErrorType

interface ErrorMessageHandler {
    fun getErrorMessage(error: ErrorType): String?
}