package es.mobiledev.feature.webscreen.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import es.mobiledev.commonandroid.ui.component.webview.component.CPTWebView
import es.mobiledev.feature.webscreen.state.WebScreenUiState

@Composable
fun WebScreenContent(
    uiState: WebScreenUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var reloadAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    CPTWebView(
        content = uiState.content,
        webViewClient = uiState.client,
        enableBackHandler = uiState.canGoBack,
        onReloadRequested = { reload ->
            reloadAction = reload
        },
        modifier = modifier.fillMaxSize(),
    )
}
