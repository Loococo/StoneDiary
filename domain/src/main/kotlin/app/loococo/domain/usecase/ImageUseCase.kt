package app.loococo.domain.usecase

import androidx.paging.PagingData
import app.loococo.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageUseCase @Inject constructor(private val imageRepository: ImageRepository) {
    operator fun invoke(): Flow<PagingData<String>> {
        return imageRepository.getImages()
    }
}
