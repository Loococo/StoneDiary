package app.loococo.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.loococo.presentation.theme.Blue
import app.loococo.presentation.theme.White

@Composable
fun StoneDiaryBottomBar(content: @Composable RowScope.() -> Unit) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        containerColor = White,
        tonalElevation = 1.dp,
        content = content
    )
}

class CustomTopArcShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val height = size.height
        val width = size.width
        val offset = with(density) { 30.dp.toPx() }

        val path = Path().apply {
            moveTo(0f, height)
            lineTo(width, height)
            lineTo(width, 0f + offset)
            quadraticTo(width * 0.5f, 0f, 0f, 0f + offset)
            lineTo(0f, 0f)
            lineTo(0f, height)
        }
        return Outline.Generic(path)
    }
}

@Composable
fun RowScope.StoneDiaryNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Blue
        ),
    )
}