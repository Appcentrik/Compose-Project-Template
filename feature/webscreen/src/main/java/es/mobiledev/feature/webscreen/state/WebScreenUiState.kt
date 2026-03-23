package es.mobiledev.feature.webscreen.state

import es.mobiledev.commonandroid.util.webview.WebViewContent
import es.mobiledev.commonandroid.util.webview.client.CPTWebViewClient

data class WebScreenUiState(
    val content: WebViewContent = WebViewContent.Unknown,
    val client: CPTWebViewClient? = null,
    val canGoBack: Boolean = false,
)
