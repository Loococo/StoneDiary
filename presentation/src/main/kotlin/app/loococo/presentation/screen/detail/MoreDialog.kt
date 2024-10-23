package app.loococo.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.loococo.presentation.R
import app.loococo.presentation.component.StoneDiaryBodyText
import app.loococo.presentation.component.StoneDiaryTitleText
import app.loococo.presentation.theme.White

@Composable
fun MoreDialog(
    visible: Boolean = false,
    currentDate: Boolean = false,
    onDismissRequest: () -> Unit,
    onModify: () -> Unit,
    onDelete: () -> Unit
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, RoundedCornerShape(10.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MoreDialogBox(
                    onClick = {
                        onDelete()
                        onDismissRequest()
                    },
                    text = R.string.delete
                )

                if (currentDate) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    MoreDialogBox(
                        onClick = {
                            onModify()
                            onDismissRequest()
                        },
                        text = R.string.modify
                    )
                }
            }
        }
    }
}

@Composable
fun MoreDialogBox(onClick: () -> Unit, text: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        StoneDiaryBodyText(text = stringResource(text), modifier = Modifier.padding(20.dp))
    }
}