package app.loococo.data.repository

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.loococo.data.paging.ImagePagingSource
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropRect
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import app.loococo.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(private val application: Application) :
    GalleryRepository {

    private val imagePagingSource = ImagePagingSource(application)

    override fun getImages(): Flow<PagingData<ImageData>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { imagePagingSource }
        ).flow
    }

    override fun getFirstImage(): ImageData {
        return imagePagingSource.getFirstImage()
    }

    override suspend fun cropAndSaveImage(
        imageData: ImageData,
        cropData: CropData,
        cropRect: CropRect
    ) {
        val contentResolver = application.contentResolver
        val originalBitmap = loadImage(contentResolver, imageData.image)

        try {
            val orientation = getImageOrientation(contentResolver, imageData.image)
            val actualCropRect =
                calculateActualCropRect(cropRect, orientation, originalBitmap, cropData.imageSize)

            require(actualCropRect.width() > 0 && actualCropRect.height() > 0) {
                "Invalid crop rectangle"
            }

            val croppedBitmap = Bitmap.createBitmap(
                originalBitmap,
                actualCropRect.left,
                actualCropRect.top,
                actualCropRect.width(),
                actualCropRect.height()
            )

            val rotatedBitmap = rotateCroppedBitmap(croppedBitmap, orientation)

            saveCroppedImage(contentResolver, rotatedBitmap)
        } catch (e: Exception) {
            originalBitmap.recycle()
            throw e
        } finally {
            originalBitmap.recycle()
        }
    }

    private fun loadImage(contentResolver: ContentResolver, imageUri: String): Bitmap {
        return contentResolver.openInputStream(Uri.parse(imageUri))?.use {
            BitmapFactory.decodeStream(it)
        } ?: throw IllegalStateException("Could not load image")
    }

    private fun getImageOrientation(contentResolver: ContentResolver, imageUri: String): Int {
        return contentResolver.openInputStream(Uri.parse(imageUri))?.use { input ->
            ExifInterface(input).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        } ?: ExifInterface.ORIENTATION_NORMAL
    }

    private fun calculateActualCropRect(
        cropRect: CropRect,
        orientation: Int,
        originalBitmap: Bitmap,
        imageSize: CropSize
    ): Rect {
        val (scaleX, scaleY) = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_ROTATE_270 ->
                originalBitmap.height.toFloat() / imageSize.width to originalBitmap.width.toFloat() / imageSize.height

            else ->
                originalBitmap.width.toFloat() / imageSize.width to originalBitmap.height.toFloat() / imageSize.height
        }

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> Rect(
                (cropRect.top * scaleX).toInt().coerceIn(0, originalBitmap.width),
                (originalBitmap.height - (cropRect.right * scaleY)).toInt()
                    .coerceIn(0, originalBitmap.height),
                (cropRect.bottom * scaleX).toInt().coerceIn(0, originalBitmap.width),
                (originalBitmap.height - (cropRect.left * scaleY)).toInt()
                    .coerceIn(0, originalBitmap.height)
            )

            ExifInterface.ORIENTATION_ROTATE_270 -> Rect(
                (originalBitmap.width - (cropRect.bottom * scaleX)).toInt()
                    .coerceIn(0, originalBitmap.width),
                (cropRect.left * scaleY).toInt().coerceIn(0, originalBitmap.height),
                (originalBitmap.width - (cropRect.top * scaleX)).toInt()
                    .coerceIn(0, originalBitmap.width),
                (cropRect.right * scaleY).toInt().coerceIn(0, originalBitmap.height)
            )

            else -> Rect(
                (cropRect.left * scaleX).toInt().coerceIn(0, originalBitmap.width),
                (cropRect.top * scaleY).toInt().coerceIn(0, originalBitmap.height),
                (cropRect.right * scaleX).toInt().coerceIn(0, originalBitmap.width),
                (cropRect.bottom * scaleY).toInt().coerceIn(0, originalBitmap.height)
            )
        }
    }

    private fun rotateCroppedBitmap(croppedBitmap: Bitmap, orientation: Int): Bitmap {
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> Bitmap.createBitmap(
                croppedBitmap,
                0,
                0,
                croppedBitmap.width,
                croppedBitmap.height,
                Matrix().apply { postRotate(90f) },
                true
            ).also { croppedBitmap.recycle() }

            ExifInterface.ORIENTATION_ROTATE_270 -> Bitmap.createBitmap(
                croppedBitmap,
                0,
                0,
                croppedBitmap.width,
                croppedBitmap.height,
                Matrix().apply { postRotate(270f) },
                true
            ).also { croppedBitmap.recycle() }

            else -> croppedBitmap
        }
    }

    private fun saveCroppedImage(contentResolver: ContentResolver, rotatedBitmap: Bitmap): String {
        val filename = "StoneDiary_${System.currentTimeMillis()}.jpg"
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/StoneDiary"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val imageUri = contentResolver.insert(imageCollection, imageDetails)
            ?: throw IllegalStateException("Failed to create new MediaStore record")

        contentResolver.openOutputStream(imageUri)?.use { stream ->
            if (!rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IllegalStateException("Failed to save bitmap")
            }
        } ?: throw IllegalStateException("Failed to open output stream")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.clear()
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(imageUri, imageDetails, null, null)
        }

        rotatedBitmap.recycle()
        return imageUri.toString()
    }

}
