package app.loococo.data.remote.api

import app.loococo.data.model.request.LoginRequest
import app.loococo.data.model.request.RegisterRequest
import app.loococo.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/users/register")
    suspend fun register(@Body data: RegisterRequest): Response<Unit>

    @POST("/users/login")
    suspend fun login(@Body data: LoginRequest): Response<LoginResponse>
}