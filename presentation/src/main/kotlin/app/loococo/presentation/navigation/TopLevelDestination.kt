package app.loococo.presentation.navigation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.loococo.presentation.utils.StoneDiaryIcons

enum class TopLevelDestination(
    val icon: ImageVector,
    val selectedColor: Color,
    val unselectedColor: Color
) {
    HOME(
        icon = StoneDiaryIcons.Home,
        selectedColor = Color.White,
        unselectedColor = Color.Gray
    ),
    CONTENT(
        icon = StoneDiaryIcons.Content,
        selectedColor = Color.White,
        unselectedColor = Color.Gray
    ),
    MY(
        icon = StoneDiaryIcons.My,
        selectedColor = Color.White,
        unselectedColor = Color.Gray
    )
}
