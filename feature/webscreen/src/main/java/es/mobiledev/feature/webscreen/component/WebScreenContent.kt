package es.mobiledev.feature.webscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.mobiledev.commonandroid.theme.BlueGrey0Light
import es.mobiledev.commonandroid.ui.component.webview.component.CPTWebView
import es.mobiledev.commonandroid.util.webview.client.rememberCPTWebViewClient
import es.mobiledev.commonandroid.util.webview.state.WebViewClientState
import es.mobiledev.feature.webscreen.state.WebScreenUiState

@Composable
fun WebScreenContent(
    uiState: WebScreenUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val client = rememberCPTWebViewClient()
    var reloadAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    CPTWebView(
        content = uiState.content,
        webViewClient = client,
        enableBackHandler = client.state.canGoBack,
        onReloadRequested = { reload ->
            reloadAction = reload
        },
        modifier = modifier.fillMaxSize(),
    )
    ManageState(
        state = client.state,
        onDismiss = onDismiss,
        onReload = { reloadAction?.invoke() }
    )
}

@Composable
private fun ManageState(
    state: WebViewClientState,
    onDismiss: () -> Unit,
    onReload: () -> Unit
) {
    if (state.hasError || state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = BlueGrey0Light),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            if (state.hasError) {
                WebViewErrorDialog(
                    onDismiss = onDismiss,
                    onReload = onReload,
                )
            }
        }
    }
}
