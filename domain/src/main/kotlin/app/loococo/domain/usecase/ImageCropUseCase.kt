package app.loococo.domain.usecase

import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropRect
import app.loococo.domain.model.image.ImageData
import app.loococo.domain.repository.ImageCropRepository
import javax.inject.Inject

class ImageCropUseCase @Inject constructor(private val imageCropRepository: ImageCropRepository) {

    fun copRect(cropData: CropData): CropRect {
        val displayedImageWidth = cropData.imageSize.width * cropData.scale
        val displayedImageHeight = cropData.imageSize.height * cropData.scale
        val horizontalMargin = (cropData.boxSize.width - displayedImageWidth) / 2
        val verticalMargin = (cropData.boxSize.height - displayedImageHeight) / 2

        val visibleLeft = (-cropData.offsetX - horizontalMargin) / cropData.scale
        val visibleTop = (-cropData.offsetY - verticalMargin) / cropData.scale
        val visibleWidth = cropData.boxSize.width / cropData.scale
        val visibleHeight = cropData.boxSize.height / cropData.scale

        return CropRect(
            left = visibleLeft.coerceIn(0f, cropData.imageSize.width.toFloat()).toInt(),
            top = visibleTop.coerceIn(0f, cropData.imageSize.height.toFloat()).toInt(),
            right = (visibleLeft + visibleWidth).coerceIn(0f, cropData.imageSize.width.toFloat())
                .toInt(),
            bottom = (visibleTop + visibleHeight).coerceIn(0f, cropData.imageSize.height.toFloat())
                .toInt()
        )
    }

    suspend fun cropImage(imageData: ImageData, cropData: CropData, cropRect: CropRect):String {
        return imageCropRepository.cropImage(imageData, cropData, cropRect)
    }
}