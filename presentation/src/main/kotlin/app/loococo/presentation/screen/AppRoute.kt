package app.loococo.presentation.screen

import kotlinx.serialization.Serializable

sealed class AppRoute {
    @Serializable
    data object Auth {
        @Serializable
        data object Login

        @Serializable
        data object Register
    }

    @Serializable
    data object Home

    @Serializable
    data object Write {
        @Serializable
        data class Emotion(val id: Long = 0L)

        @Serializable
        data class Content(val emotion: String, val id: Long = 0L)
    }

    @Serializable
    data object Gallery

    @Serializable
    data class Detail(val id: Long)
}