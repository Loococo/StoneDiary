package app.loococo.data.repository

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.util.Base64
import androidx.exifinterface.media.ExifInterface
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropRect
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData
import app.loococo.domain.repository.ImageCropRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageCropRepositoryImpl @Inject constructor(private val application: Application) :
    ImageCropRepository {
    override suspend fun cropImage(
        imageData: ImageData,
        cropData: CropData,
        cropRect: CropRect
    ): String {
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
            return bitmapToBase64(rotatedBitmap)
        } catch (e: Exception) {
            originalBitmap.recycle()
            return ""
        } finally {
            originalBitmap.recycle()
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        return ByteArrayOutputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
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
}