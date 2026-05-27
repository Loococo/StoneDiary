package app.loococo.domain.model

import kotlinx.serialization.Serializable

/**
 * 로그인 응답에 담기는 사용자 + 토큰 묶음.
 */
data class LoginData(
    val user: User,
    val tokens: Tokens
)

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String? = null
)

@Serializable
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
