package app.loococo.data.repository

import app.loococo.data.model.request.LoginRequest
import app.loococo.data.remote.manger.AuthDataSource
import app.loococo.domain.model.LoginData
import app.loococo.domain.model.network.Resource
import app.loococo.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    LoginRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<LoginData>> {
        val request = LoginRequest(email, password)
        return authDataSource.login(request)
    }
}