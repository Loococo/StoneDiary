package app.loococo.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.loococo.presentation.theme.StoneDiaryTheme


@Composable
fun StoneDiaryApp(
    appState: StoneDiaryAppState = rememberStoneDiaryAppState()
) {
        StoneDiaryTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
            ) { padding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    StoneDiaryNavHost(appState)
                }

            }
    }
}