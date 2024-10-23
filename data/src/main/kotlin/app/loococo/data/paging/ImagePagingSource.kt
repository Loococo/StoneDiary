package app.loococo.data.paging

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState

class ImagePagingSource(private val context: Context) : PagingSource<Int, String>() {

    private val imageUris: List<String> by lazy { loadAllImages(context) }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize

            val startIndex = page * pageSize
            val endIndex = minOf(startIndex + pageSize, imageUris.size)

            val images = imageUris.subList(startIndex, endIndex)

            LoadResult.Page(
                data = images,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (endIndex == imageUris.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun loadAllImages(context: Context): List<String> {
        val uriList = mutableListOf<String>()
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val cursor = context.contentResolver.query(
            contentUri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val imageUri = ContentUris.withAppendedId(contentUri, id)
                uriList.add(imageUri.toString())
            }
        }
        return uriList
    }

    fun getFirstImage(): String {
        return imageUris.firstOrNull() ?: ""
    }
}
