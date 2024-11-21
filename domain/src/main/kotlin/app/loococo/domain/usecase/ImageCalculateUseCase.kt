package app.loococo.domain.usecase

import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import javax.inject.Inject

class ImageCalculateUesCase @Inject constructor() {

    fun calculateImageSize(imageData: ImageData, boxSize: CropSize): CropSize {
        val imageWidth = imageData.width
        val imageHeight = imageData.height

        val boxWidth = boxSize.width.toFloat()
        val boxHeight = boxSize.height.toFloat()

        val imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()

        val (scaledWidth, scaledHeight) = if (boxWidth >= imageWidth && boxHeight >= imageHeight) {
            imageWidth to imageHeight
        } else {
            if (imageAspectRatio > boxWidth / boxHeight) {
                val scale = boxWidth / imageWidth.toFloat()
                imageWidth * scale to imageHeight * scale
            } else {
                val scale = boxHeight / imageHeight.toFloat()
                imageWidth * scale to imageHeight * scale
            }
        }
        return CropSize(scaledWidth.toInt(), scaledHeight.toInt())
    }


    fun calculateScaleFactor(imageSize: CropSize, boxSize: CropSize): Float {
        return if (imageSize.width > imageSize.height) {
            boxSize.height.toFloat() / imageSize.height.toFloat()
        } else {
            boxSize.width.toFloat() / imageSize.width.toFloat()
        }
    }
}