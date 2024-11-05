package app.loococo.data.paging

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.loococo.domain.model.image.ImageData

class ImagePagingSource(private val context: Context) : PagingSource<Int, ImageData>() {

    private val imageUris: List<ImageData> by lazy { loadAllImages(context) }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
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

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun loadAllImages(context: Context): List<ImageData> {
        val uriList = mutableListOf<ImageData>()
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )

        val cursor = context.contentResolver.query(
            contentUri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val imageUri = ContentUris.withAppendedId(contentUri, id).toString()
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)

                val inputStream = context.contentResolver.openInputStream(ContentUris.withAppendedId(contentUri, id))
                val exif = ExifInterface(inputStream!!)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

                val (adjustedWidth, adjustedHeight) = when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90,
                    ExifInterface.ORIENTATION_ROTATE_270 -> height to width
                    else -> width to height
                }

                uriList.add(ImageData(imageUri, adjustedWidth, adjustedHeight))
            }
        }
        return uriList
    }


    fun getFirstImage(): ImageData {
        return imageUris.firstOrNull() ?: ImageData()
    }
}