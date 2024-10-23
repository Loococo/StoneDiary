package app.loococo.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.loococo.data.paging.ImagePagingSource
import app.loococo.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(val application: Application) : ImageRepository {
    override fun getImages(): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ImagePagingSource(application) }
        ).flow
    }
}