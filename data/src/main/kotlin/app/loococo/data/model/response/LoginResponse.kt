package app.loococo.data.model.response

import app.loococo.domain.model.LoginData
import app.loococo.domain.model.Tokens
import app.loococo.domain.model.User

data class LoginResponse(
    val user: UserResponse,
    val tokens: TokensResponse
)

data class UserResponse(
    val id: String,
    val email: String,
    val name: String?,
    val createdAt: String
)

data class TokensResponse(
    val accessToken: String,
    val refreshToken: String
)

fun LoginResponse.toLoginData(): LoginData {
    return LoginData(
       user = User(
           id = user.id,
           email =  user.email,
           name = user.name
       ),
        tokens = Tokens(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken
        )
    )
}
