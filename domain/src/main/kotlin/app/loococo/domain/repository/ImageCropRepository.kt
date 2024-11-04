package app.loococo.domain.repository

import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropRect
import app.loococo.domain.model.image.ImageData

interface ImageCropRepository {
    suspend fun cropImage(imageData: ImageData, cropData: CropData, cropRect: CropRect):String
}