package app.loococo.domain.usecase

import androidx.paging.PagingData
import app.loococo.domain.model.image.ImageData
import app.loococo.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryUseCase @Inject constructor(private val galleryRepository: GalleryRepository) {

    fun getImages(): Flow<PagingData<ImageData>> {
        return galleryRepository.getImages()
    }

    fun getFirstImage(): ImageData {
        return galleryRepository.getFirstImage()
    }
}
