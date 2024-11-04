package app.loococo.domain.repository

import androidx.paging.PagingData
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropRect
import app.loococo.domain.model.image.ImageData
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getImages(): Flow<PagingData<ImageData>>
    fun getFirstImage(): ImageData
    suspend fun cropAndSaveImage(
        imageData: ImageData,
        cropData: CropData,
        cropRect: CropRect
    )
}