package app.loococo.data.remote.manger

import app.loococo.data.model.request.LoginRequest
import app.loococo.data.model.request.RegisterRequest
import app.loococo.data.model.response.toLoginData
import app.loococo.data.remote.api.AuthApi
import app.loococo.domain.error.ErrorMapper
import app.loococo.domain.model.LoginData
import app.loococo.domain.model.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val errorMapper: ErrorMapper
) {

    suspend fun register(request: RegisterRequest): Flow<Resource<Unit>> =
        suspendResponseResult(errorMapper) { authApi.register(request) }

    suspend fun login(request: LoginRequest): Flow<Resource<LoginData>> =
        suspendResponseResult(errorMapper, { authApi.login(request) }) { it.toLoginData() }

}