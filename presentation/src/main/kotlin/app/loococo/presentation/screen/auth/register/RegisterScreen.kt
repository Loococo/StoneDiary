package app.loococo.presentation.screen.auth.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.loococo.presentation.R
import app.loococo.presentation.component.CircularProgressBar
import app.loococo.presentation.component.HeightSpacer
import app.loococo.presentation.component.StoneDiaryBorderEmailTextField
import app.loococo.presentation.component.StoneDiaryBorderNameTextField
import app.loococo.presentation.component.StoneDiaryBorderPasswordTextField
import app.loococo.presentation.component.StoneDiaryRoundButton
import app.loococo.presentation.component.StoneDiaryTitleText
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RegisterRoute() {
    RegisterScreen()
}

@Composable
fun RegisterScreen() {
    val viewModel: RegisterViewModel = hiltViewModel()
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is RegisterSideEffect.ShowToast -> {
                Toast.makeText(context, it.res, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterHeader()
        RegisterContent(
            email = state.email,
            password = state.password,
            name = state.name,
            onEventSent = viewModel::onEventReceived
        )
    }

    CircularProgressBar(state.isLoading)
}


@Composable
fun RegisterHeader() {
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
fun RegisterContent(
    email: String,
    password: String,
    name: String,
    onEventSent: (RegisterEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HeightSpacer(height = 20)

        StoneDiaryBorderEmailTextField(
            text = email,
            hint = stringResource(R.string.email_hint),
            onValueChange = {
                onEventSent(RegisterEvent.OnEmailUpdated(it))
            }
        )

        HeightSpacer(height = 10)

        StoneDiaryBorderPasswordTextField(
            text = password,
            hint = stringResource(R.string.password_hint),
            imeAction = ImeAction.Next,
            onValueChange = {
                onEventSent(RegisterEvent.OnPasswordUpdated(it))
            }
        )

        HeightSpacer(height = 10)

        StoneDiaryBorderNameTextField(
            text = name,
            hint = stringResource(R.string.name_hint),
            onValueChange = {
                onEventSent(RegisterEvent.OnNameUpdated(it))
            }
        )

        HeightSpacer(height = 20)

        StoneDiaryRoundButton(
            text = stringResource(R.string.register),
            onClick = {
                onEventSent(RegisterEvent.OnRegisterClicked)
            }
        )

        HeightSpacer(height = 20)
    }
}
