package app.loococo.data.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import app.loococo.domain.repository.ImageSaveRepository
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImageSaveRepositoryImpl @Inject constructor(private val application: Application) :
    ImageSaveRepository {

    override fun saveCropImage(image: String): String {
        return try {
            val bitmap = base64ToBitmap(image)

            val fileName = "stone_${UUID.randomUUID()}_${System.currentTimeMillis()}.jpg"
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

    private fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}

