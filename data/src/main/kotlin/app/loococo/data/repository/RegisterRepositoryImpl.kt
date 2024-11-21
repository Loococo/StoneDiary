package app.loococo.data.repository

import app.loococo.data.model.request.RegisterRequest
import app.loococo.data.remote.manger.AuthDataSource
import app.loococo.domain.model.network.Resource
import app.loococo.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
    RegisterRepository {

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<Unit>> {
        val request = RegisterRequest(email, password, name)
        return authDataSource.register(request)
    }
}