package app.loococo.domain.usecase

import app.loococo.domain.repository.ImageSaveRepository
import javax.inject.Inject

class ImageSaveUseCase @Inject constructor(private val imageSaveRepository: ImageSaveRepository) {

    fun saveCropImage(image:String):String {
        return imageSaveRepository.saveCropImage(image)
    }

    suspend fun saveImages(uri: List<String>): List<String> {
        return imageSaveRepository.saveImages(uri)
    }
}