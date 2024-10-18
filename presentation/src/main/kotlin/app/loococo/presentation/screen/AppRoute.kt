package app.loococo.presentation.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    object Home

    @Serializable
    object Write {
        @Serializable
        object Emotion

        @Serializable
        data class Content(val emotion: String)
    }

    @Serializable
    object Gallery

    @Serializable
    data class Detail(val id: Long)
}