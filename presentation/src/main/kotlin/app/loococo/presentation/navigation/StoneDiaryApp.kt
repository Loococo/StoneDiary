package app.loococo.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.loococo.presentation.component.StoneDiaryBottomBar
import app.loococo.presentation.component.StoneDiaryNavigationBarItem
import app.loococo.presentation.theme.StoneDiaryTheme

@Composable
fun StoneDiaryApp(
    appState: StoneDiaryAppState = rememberStoneDiaryAppState()
) {
    StoneDiaryTheme {
        Scaffold(
            bottomBar = {
                StoneDiaryBottomBar {
                    appState.topLevelDestinations.forEach { destination ->
                        StoneDiaryNavigationBarItem(
                            selected = destination == appState.currentTopLevelDestination,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                StoneDiaryIcon(
                                    icon = destination.icon,
                                    color = destination.unselectedColor
                                )
                            },
                            selectedIcon = {
                                StoneDiaryIcon(
                                    icon = destination.icon,
                                    color = destination.selectedColor
                                )
                            }
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                StoneDiaryNavHost(appState)
            }
        }
    }
}

@Composable
fun StoneDiaryIcon(icon: ImageVector, color: Color) {
    Icon(
        modifier = Modifier.size(25.dp),
        imageVector = icon,
        tint = color,
        contentDescription = null
    )
}