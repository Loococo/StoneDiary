package app.loococo.data.remote.manger

import app.loococo.domain.error.ErrorMapper
import app.loococo.domain.model.network.error.ErrorHandler
import app.loococo.domain.model.network.error.ErrorType
import app.loococo.domain.model.network.Resource
import com.google.gson.Gson
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException


suspend fun <T : Any> suspendResponseResult(
    errorMapper: ErrorMapper,
    execute: suspend () -> Response<T>
): Flow<Resource<T>> = flow {
    try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(Resource.Success(body))
            } else {
                emit(Resource.Error(ErrorType.NetworkError.UnknownException))
            }
        } else {
            response.errorBody()?.let {
                val errorHandler = Gson().fromJson(it.charStream(), ErrorHandler::class.java)
                emit(Resource.Error(errorMapper.map(errorHandler)))
            } ?: run {
                emit(Resource.Error(ErrorType.NetworkError.UnknownException))
            }
        }
    } catch (e: Exception) {
        emit(
            Resource.Error(
                when (e) {
                    is HttpException -> ErrorType.NetworkError.UnknownException
                    is TimeoutCancellationException -> ErrorType.NetworkError.NetworkErrorException
                    is ConnectException -> ErrorType.NetworkError.NetworkErrorException
                    is IOException -> ErrorType.NetworkError.NetworkErrorException
                    else -> ErrorType.NetworkError.UnknownException
                }
            )
        )
        emit(Resource.Error(ErrorType.NetworkError.NetworkErrorException))
    }
}.catch {
    emit(Resource.Error(ErrorType.NetworkError.NetworkErrorException))
}

suspend fun <T : Any, R : Any> suspendResponseResult(
    errorMapper: ErrorMapper,
    execute: suspend () -> Response<T>,
    mapResponse: (T) -> R = { it as R }
): Flow<Resource<R>> = flow {
    try {
        val response = execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(Resource.Success(mapResponse(body)))
            } else {
                emit(Resource.Error(ErrorType.NetworkError.UnknownException))
            }
        } else {
            response.errorBody()?.let {
                val errorHandler = Gson().fromJson(it.charStream(), ErrorHandler::class.java)
                emit(Resource.Error(errorMapper.map(errorHandler)))
            } ?: run {
                emit(Resource.Error(ErrorType.NetworkError.UnknownException))
            }
        }
    } catch (e: Exception) {
        emit(
            Resource.Error(
                when (e) {
                    is HttpException -> ErrorType.NetworkError.UnknownException
                    is TimeoutCancellationException -> ErrorType.NetworkError.NetworkErrorException
                    is ConnectException -> ErrorType.NetworkError.NetworkErrorException
                    is IOException -> ErrorType.NetworkError.NetworkErrorException
                    else -> ErrorType.NetworkError.UnknownException
                }
            )
        )
    }
}.catch {
    emit(Resource.Error(ErrorType.NetworkError.NetworkErrorException))
}
