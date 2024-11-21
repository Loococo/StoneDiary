package app.loococo.domain.repository

import app.loococo.domain.model.LoginData
import app.loococo.domain.model.network.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(email: String, password: String): Flow<Resource<LoginData>>
}