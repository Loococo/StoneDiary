package app.loococo.presentation.screen.write.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.loococo.presentation.R
import app.loococo.presentation.component.StoneDiaryBodyText
import app.loococo.presentation.component.StoneDiaryTitleText
import app.loococo.presentation.theme.White

@Composable
fun DeleteImageDialog(
    visible: Boolean = false,
    onImageDeleted: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (visible) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .background(White, RoundedCornerShape(10.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StoneDiaryTitleText(
                    text = stringResource(R.string.delete_image_waring),
                    modifier = Modifier.padding(30.dp)
                )
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDismissRequest()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        StoneDiaryBodyText(
                            text = stringResource(R.string.cancel),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onImageDeleted()
                                onDismissRequest()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        StoneDiaryBodyText(
                            text = stringResource(R.string.delete),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}