package app.loococo.domain.model

data class LoginData(
    val user: User,
    val tokens: Tokens
)

data class User(
    val id: String,
    val email: String,
    val name: String?
)

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
