package es.mobiledev.commonandroid.util.webview.state

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * CPT design utility
 *
 * WebViewClientState is an abstract class that represents the state of a WebView.
 *
 * It provides observable properties to track the loading status, errors, and navigation
 * capabilities of the WebView.
 */
abstract class WebViewClientState {
    /**
     * A flag that indicates if the WebView is currently loading content.
     */
    var isLoading by mutableStateOf(false)
        internal set

    /**
     * A flag that indicates if an error occurred while loading content.
     */
    var hasError by mutableStateOf(false)
        internal set

    /**
     * A flag that indicates if the WebView can navigate back in its history.
     */
    var canGoBack by mutableStateOf(false)
        internal set

    internal fun setLoadingState() {
        isLoading = true
        hasError = false
    }

    internal fun setFinishedState() {
        isLoading = false
    }

    internal fun setErrorState(
        debugMsg: String
    ) {
        Log.e("WebViewClient", debugMsg)

        isLoading = false
        hasError = true
    }
}
