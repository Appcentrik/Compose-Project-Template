package es.mobiledev.feature.webscreen.state

import es.mobiledev.commonandroid.util.webview.WebViewContent

data class WebScreenUiState(
    val content: WebViewContent = WebViewContent.Unknown
)
