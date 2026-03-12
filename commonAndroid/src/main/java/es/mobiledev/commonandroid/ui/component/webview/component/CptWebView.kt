package es.mobiledev.commonandroid.ui.component.webview.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import es.mobiledev.commonandroid.util.webview.WebViewContent
import es.mobiledev.commonandroid.util.webview.client.CPTWebViewClient
import es.mobiledev.commonandroid.util.webview.loadAsset
import es.mobiledev.commonandroid.util.webview.loadHtml
import java.lang.ref.WeakReference

/**
 * CPT design component
 *
 * CPTWebView is a wrapper around the Android WebView that integrates with Jetpack Compose.
 *
 * This component simplifies the usage of WebView in a Compose environment, handling common tasks
 * like loading different types of content and managing back navigation.
 *
 * @param content the [WebViewContent] to be displayed in the WebView
 * @param modifier the [Modifier] to be applied to this component
 * @param webViewClient a custom [CPTWebViewClient] to manage WebView events
 * @param enableBackHandler a flag that indicates if the component should handle back navigation
 * @param onReloadRequested a callback that provides a reload function to be invoked externally
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun CPTWebView(
    content: WebViewContent,
    modifier: Modifier = Modifier,
    webViewClient: CPTWebViewClient? = null,
    enableBackHandler: Boolean = false,
    onReloadRequested: ((reload: () -> Unit) -> Unit)? = null
) {
    var webViewRef by remember { mutableStateOf<WeakReference<WebView?>>(WeakReference(null)) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                webViewRef = WeakReference(this)
                settings.javaScriptEnabled = true
                this.webViewClient = webViewClient?.apply {
                    setAssetLoader(context)
                } ?: WebViewClient()
                when (content) {
                    is WebViewContent.Url -> loadUrl(content.url)
                    is WebViewContent.Html -> loadHtml(content.data)
                    is WebViewContent.Asset -> loadAsset(content.url)
                    is WebViewContent.Unknown -> {
                        // NO-OP
                    }
                }
            }
        }
    )

    onReloadRequested?.invoke {
        webViewRef.get()?.reload()
    }

    BackHandler(enabled = enableBackHandler) {
        webViewRef.get()?.goBack()
    }
}
