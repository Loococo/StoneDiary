package app.loococo.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.loococo.data.paging.ImagePagingSource
import app.loococo.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val application: Application) : ImageRepository {

    private val imagePagingSource = ImagePagingSource(application)

    override fun getImages(): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { imagePagingSource }
        ).flow
    }

    override fun getFirstImage(): String {
        return imagePagingSource.getFirstImage() // 첫 번째 이미지 반환
    }
}
