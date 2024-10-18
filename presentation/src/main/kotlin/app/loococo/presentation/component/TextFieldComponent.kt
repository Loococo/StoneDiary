package app.loococo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun StoneDiaryTitleTextField(text: String, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf(text) }

    BasicTextField(
        value = value,
        onValueChange = { newTextState ->
            if (newTextState.lines().size <= 2) {
                value = newTextState
                onValueChange(value)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = "제목을 입력해 주세요.",
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            innerTextField()
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle(
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        ),
        maxLines = 2
    )
}

@Composable
fun StoneDiaryContentTextField(text: String, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf(text) }

    BasicTextField(
        value = value,
        onValueChange = { newTextState ->
            value = newTextState
            onValueChange(value)
        },
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = "오늘의 일기를 작성해 주세요.\n" +
                            "아래 사진을 추가할 수 있습니다.",
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            innerTextField()
        },
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Default,
//            keyboardType = KeyboardType.Text
//        ),
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        ),
    )
}