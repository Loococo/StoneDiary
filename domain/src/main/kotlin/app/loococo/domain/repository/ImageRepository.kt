package app.loococo.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImages(): Flow<PagingData<String>>
}