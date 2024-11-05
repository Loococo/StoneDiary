package app.loococo.domain.model.image

data class CropData(
    val boxSize: CropSize = CropSize(),
    val imageSize: CropSize = CropSize(),
    val scale: Float = 0f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f
)

data class CropSize(
    val width: Int = 0,
    val height: Int = 0
)

data class CropRect(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)