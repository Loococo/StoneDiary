package app.loococo.domain.model.network

import app.loococo.domain.model.network.error.ErrorType

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: ErrorType) : Resource<Nothing>()
}