package app.loococo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.loococo.presentation.theme.Black
import app.loococo.presentation.theme.Gray
import app.loococo.presentation.theme.White

@Composable
fun StoneDiaryTitleTextField(text: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = text,
        onValueChange = { newTextState ->
            if (newTextState.lines().size <= 2) {
                onValueChange(newTextState)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = "제목을 입력해 주세요.",
                    color = Gray,
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
            fontSize = 24.sp,
            color = Black,
            textAlign = TextAlign.Start
        ),
        maxLines = 2
    )
}

@Composable
fun StoneDiaryContentTextField(text: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = text,
        onValueChange = { newTextState ->
            onValueChange(newTextState)
        },
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    text = "오늘의 일기를 작성해 주세요.\n" +
                            "아래 사진을 추가할 수 있습니다.",
                    color = Gray,
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            innerTextField()
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Black,
            textAlign = TextAlign.Start
        ),
    )
}


@Composable
fun StoneDiaryBorderTextField(
    text: String,
    hint: String,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .border(1.dp, Black, RoundedCornerShape(10.dp))
            .padding(10.dp, 5.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Black,
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(Black),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Text(
                        text = hint,
                        color = Gray,
                        style = TextStyle(
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation
        )
    }
}

@Composable
fun StoneDiaryBorderEmailTextField(
    text: String,
    hint: String,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (String) -> Unit
) {
    StoneDiaryBorderTextField(
        text = text,
        hint = hint,
        imeAction = imeAction,
        keyboardType = KeyboardType.Email,
        onValueChange = onValueChange
    )
}

@Composable
fun StoneDiaryBorderPasswordTextField(
    text: String,
    hint: String,
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (String) -> Unit
) {
    StoneDiaryBorderTextField(
        text = text,
        hint = hint,
        imeAction = imeAction,
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = onValueChange
    )
}

@Composable
fun StoneDiaryBorderNameTextField(
    text: String,
    hint: String,
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (String) -> Unit
) {
    StoneDiaryBorderTextField(
        text = text,
        hint = hint,
        imeAction = imeAction,
        keyboardType = KeyboardType.Text,
        onValueChange = onValueChange
    )
}