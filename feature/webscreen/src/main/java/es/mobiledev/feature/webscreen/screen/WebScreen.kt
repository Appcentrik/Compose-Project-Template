package es.mobiledev.feature.webscreen.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.util.webview.WebViewContent
import es.mobiledev.feature.webscreen.component.WebScreenContent
import es.mobiledev.feature.webscreen.viewmodel.WebScreenViewModel

@Composable
fun WebScreen(
    onNavigateBack: () -> Unit,
    viewModel: WebScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.getUiState().collectAsStateWithLifecycle()

    BaseScreen(
        isLoading = uiState.isLoading
    ) {
        // TODO: Change this for error screen
        if (uiState.data.content is WebViewContent.Unknown) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("We were unable to load the content", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = onNavigateBack
                ) {
                    Text("Go back")
                }
            }
        } else {
            WebScreenContent(
                uiState = uiState.data,
                onDismiss = onNavigateBack
            )
        }
    }
}
