package app.loococo.domain.ext


fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return this.isNotEmpty() && this.matches(emailRegex)
}

fun String.isValidPassword(): Boolean {
    return this.isNotEmpty() && this.length in 8..32
}

fun String.isValidName(): Boolean {
    return this.isNotEmpty() && this.length in 2..50
}