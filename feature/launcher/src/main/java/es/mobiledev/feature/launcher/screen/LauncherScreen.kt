package es.mobiledev.feature.launcher.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.feature.launcher.R
import es.mobiledev.feature.launcher.component.LauncherScreenContent
import es.mobiledev.feature.launcher.viewmodel.LauncherViewModel

@Composable
fun LauncherScreen(
    onLauncherFinished: () -> Unit,
) {
    val launcherViewModel: LauncherViewModel = hiltViewModel()
    val uiState by launcherViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.data.hasFinish) {
        if (uiState.data.hasFinish) {
            onLauncherFinished()
        }
    }

    BaseScreen { paddingValues ->
        LauncherScreenContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(id = R.drawable.launcher_background),
                        contentScale = ContentScale.Crop,
                    ).padding(paddingValues),
        )
    }
}

@Composable
@PreviewLightDark
private fun Preview() {
    CPTTheme {
        LauncherScreen(onLauncherFinished = {})
    }
}
