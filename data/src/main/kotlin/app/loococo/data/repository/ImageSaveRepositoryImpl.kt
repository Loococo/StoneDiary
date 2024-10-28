package app.loococo.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import app.loococo.domain.repository.ImageSaveRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageSaveRepositoryImpl @Inject constructor(private val application: Application) :
    ImageSaveRepository {

    override suspend fun saveImages(uri: List<String>): List<String> {
        val savedImages = mutableListOf<String>()

        uri.forEach {
            val savedImagePath = saveImage(it)
            if (savedImagePath.isNotBlank()) {
                savedImages.add(savedImagePath)
            }
        }

        return savedImages
    }

    private fun saveImage(uri: String): String {
        return try {
            val source = ImageDecoder.createSource(application.contentResolver, Uri.parse(uri))
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }

            val fileName = "stone_${System.currentTimeMillis()}.jpg"
            val file = File(application.filesDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            bitmap.recycle()

            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }
}

