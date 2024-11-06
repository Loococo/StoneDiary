package app.loococo.presentation.screen.gallery.helper

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.loococo.domain.model.image.CropData
import app.loococo.domain.model.image.CropSize
import app.loococo.domain.model.image.ImageData

class ImageZoomState {
    var scale by mutableFloatStateOf(1f)
    var offsetX by mutableFloatStateOf(0f)
    var offsetY by mutableFloatStateOf(0f)
    var imageSize by mutableStateOf(CropSize())
    var initialScale by mutableFloatStateOf(1f)
}

class ImageZoomHelper(
    private val onUpdateZoomData: (CropData) -> Unit
) {
    private val state = ImageZoomState()

    fun initializeZoom(
        imageData: ImageData,
        boxSize: CropSize,
        calculateImageSize: (ImageData, CropSize) -> CropSize,
        calculateScaleFactor: (CropSize, CropSize) -> Float
    ) {
        state.imageSize = calculateImageSize(imageData, boxSize)
        state.initialScale = calculateScaleFactor(state.imageSize, boxSize)
        state.scale = state.initialScale
        state.offsetX = 0f
        state.offsetY = 0f

        updateZoomData(boxSize)
    }

    fun handleZoomPan(
        zoom: Float,
        panX: Float,
        panY: Float,
        centroidX: Float,
        centroidY: Float,
        boxSize: CropSize
    ) {
        val newScale = (state.scale * zoom).coerceIn(state.initialScale, 5f)

        val newOffsetX = state.offsetX + panX +
                (centroidX - boxSize.width / 2) * (newScale - state.scale)
        val newOffsetY = state.offsetY + panY +
                (centroidY - boxSize.height / 2) * (newScale - state.scale)

        val maxOffsetX = ((state.imageSize.width * newScale - boxSize.width) / 2).coerceAtLeast(0f)
        val maxOffsetY =
            ((state.imageSize.height * newScale - boxSize.height) / 2).coerceAtLeast(0f)

        state.scale = newScale
        state.offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
        state.offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)

        updateZoomData(boxSize)
    }

    private fun updateZoomData(boxSize: CropSize) {
        onUpdateZoomData(
            CropData(
                boxSize,
                state.imageSize,
                state.scale,
                state.offsetX,
                state.offsetY
            )
        )
    }

    fun getTransformationState() = TransformationState(
        scale = state.scale,
        offsetX = state.offsetX,
        offsetY = state.offsetY
    )
}

data class TransformationState(
    val scale: Float,
    val offsetX: Float,
    val offsetY: Float
)