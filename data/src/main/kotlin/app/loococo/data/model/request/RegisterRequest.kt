package app.loococo.data.model.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)