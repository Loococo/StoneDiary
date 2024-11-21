package app.loococo.domain.repository

import app.loococo.domain.model.network.Resource
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(email: String, password: String, name: String): Flow<Resource<Unit>>
}