package app.loococo.domain.usecase

import app.loococo.domain.repository.IImageSaveRepository
import javax.inject.Inject

class ImageSaveUseCase @Inject constructor(private val imageSaveRepository: IImageSaveRepository) {

    fun saveCropImage(image:String):String {
        return imageSaveRepository.saveCropImage(image)
    }
}