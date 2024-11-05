package app.loococo.domain.repository

interface ImageSaveRepository {
    suspend fun saveImages(uri: List<String>): List<String>
    fun saveCropImage(image: String):String
}