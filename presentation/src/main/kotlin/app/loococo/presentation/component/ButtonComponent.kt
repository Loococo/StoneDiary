package app.loococo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.loococo.presentation.theme.Black
import app.loococo.presentation.theme.White

@Composable
fun StoneDiaryNavigationButton(
    size: Dp,
    icon: ImageVector,
    description: String,
    color: Color = Black,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(size),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(size),
            tint = color
        )
    }
}

@Composable
fun StoneDiaryRoundButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black,
            contentColor = White,
        ),
    ) {
        StoneDiaryBodyText(
            text = text,
            color = White,
            modifier = Modifier.padding(10.dp)
        )
    }
}