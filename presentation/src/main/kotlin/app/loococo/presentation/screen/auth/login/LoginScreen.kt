package app.loococo.presentation.screen.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.presentation.R
import app.loococo.presentation.component.CircularProgressBar
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryBodyText
import app.loococo.presentation.component.StoneDiaryBorderEmailTextField
import app.loococo.presentation.component.StoneDiaryBorderPasswordTextField
import app.loococo.presentation.component.StoneDiaryRoundButton
import app.loococo.presentation.component.StoneDiaryTitleText
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginRoute(
    navigateUpToHome: () -> Unit,
    navigateUpToRegister: () -> Unit
) {
    LoginScreen(navigateUpToHome, navigateUpToRegister)
}

@Composable
fun LoginScreen(
    navigateUpToHome: () -> Unit,
    navigateUpToRegister: () -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            LoginSideEffect.NavigateToHome -> navigateUpToHome()
            LoginSideEffect.NavigateToRegister -> navigateUpToRegister()
            is LoginSideEffect.ShowToast -> {
                Toast.makeText(context, it.res, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeader()
            LoginContent(
                email = state.email,
                password = state.password,
                onEventSent = viewModel::onEventReceived
            )
            LoginSubContent(
                onEventSent = viewModel::onEventReceived
            )
        }
        LoginFooter(
            onEventSent = viewModel::onEventReceived
        )
    }

    CircularProgressBar(state.isLoading)
}

@Composable
fun LoginHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(R.drawable.ic_stone_happy),
            contentDescription = ""
        )
        StoneDiaryTitleText(stringResource(R.string.app_name))
    }
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEventSent: (LoginEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HeightSpacer(height = 20)

        StoneDiaryBorderEmailTextField(
            text = email,
            hint = stringResource(R.string.email_hint),
            onValueChange = {
                onEventSent(LoginEvent.OnEmailUpdated(it))
            }
        )

        HeightSpacer(height = 10)

        StoneDiaryBorderPasswordTextField(
            text = password,
            hint = stringResource(R.string.password_hint),
            onValueChange = {
                onEventSent(LoginEvent.OnPasswordUpdated(it))
            }
        )
        HeightSpacer(height = 20)

        StoneDiaryRoundButton(
            text = stringResource(R.string.login),
            onClick = {
                onEventSent(LoginEvent.OnLoginClicked)
            }
        )

        HeightSpacer(height = 20)
    }
}

@Composable
fun LoginSubContent(onEventSent: (LoginEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StoneDiaryBodyText("비밀번호찾기")
        StoneDiaryBodyText(
            text = "회원가입",
            modifier = Modifier.clickable { onEventSent(LoginEvent.OnRegisterClicked) }
        )
    }
}

@Composable
fun ColumnScope.LoginFooter(onEventSent: (LoginEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(.1f)
            .padding(10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        StoneDiaryBodyText(
            text = "로그인 없이 사용하기",
            modifier = Modifier.clickable { onEventSent(LoginEvent.OnSkipLoginClicked) }
        )
    }
}