package app.loococo.domain.repository

interface IImageSaveRepository {
    fun saveCropImage(image: String):String
}