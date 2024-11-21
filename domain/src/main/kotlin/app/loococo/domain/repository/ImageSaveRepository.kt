package app.loococo.domain.repository

interface ImageSaveRepository {
    fun saveCropImage(image: String):String
}