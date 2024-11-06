package app.loococo.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import app.loococo.presentation.theme.White

@Composable
fun DrawGuidelines() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val (width, height) = size

        drawGuidelineLines(width, height, isVertical = true)
        drawGuidelineLines(width, height, isVertical = false)
    }
}

fun DrawScope.drawGuidelineLines(width: Float, height: Float, isVertical: Boolean) {
    repeat(2) { i ->
        val position = (i + 1) * (if (isVertical) width else height) / 3
        drawLine(
            color = White,
            start = if (isVertical) Offset(position, 0f) else Offset(0f, position),
            end = if (isVertical) Offset(position, height) else Offset(width, position),
            strokeWidth = 2f
        )
    }
}