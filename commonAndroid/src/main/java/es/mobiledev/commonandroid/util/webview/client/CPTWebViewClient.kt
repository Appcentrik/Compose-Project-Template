package es.mobiledev.commonandroid.util.webview.client

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.webkit.WebViewAssetLoader
import es.mobiledev.commonandroid.util.webview.ASSETS_PATH
import es.mobiledev.commonandroid.util.webview.state.CPTWebViewClientState
import es.mobiledev.commonandroid.util.webview.state.WebViewClientState

/**
 * CPT design utility
 *
 * rememberCPTWebViewClient creates and remembers a [CPTWebViewClient] instance.
 *
 * This function provides a convenient way to manage the state of a WebView within a Composable.
 *
 * @return a remembered instance of [CPTWebViewClient]
 */
@Composable
fun rememberCPTWebViewClient(): CPTWebViewClient {
    val state = remember { CPTWebViewClientState() }
    return remember { object : CPTWebViewClient(state) {} }
}

/**
 * CPT design utility
 *
 * CPTWebViewClient is a custom [WebViewClient] that tracks the loading state of a WebView.
 *
 * This class integrates with [WebViewClientState] to notify about page loading events,
 * errors, and navigation history updates. It also supports local asset loading.
 *
 * @param state the [WebViewClientState] used to track the status of the WebView
 * @param assetLoader an optional [WebViewAssetLoader] for handling local asset requests
 */
abstract class CPTWebViewClient(
    val state: WebViewClientState,
    internal var assetLoader: WebViewAssetLoader? = null
) : WebViewClient() {
    override fun onPageStarted(
        view: WebView?,
        url: String?,
        favicon: Bitmap?
    ) {
        super.onPageStarted(view, url, favicon)
        state.setLoadingState()
    }

    override fun onPageFinished(
        view: WebView?,
        url: String?
    ) {
        super.onPageFinished(view, url)
        state.setFinishedState()
    }

    override fun doUpdateVisitedHistory(
        view: WebView?,
        url: String?,
        isReload: Boolean
    ) {
        super.doUpdateVisitedHistory(view, url, isReload)
        state.canGoBack = view?.canGoBack() == true
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (request?.isForMainFrame == true) {
            state.setErrorState(
                debugMsg = "Error: ${error?.errorCode} -> ${error?.description}"
            )
        }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        if (request?.isForMainFrame == true) {
            state.setErrorState(
                debugMsg = "Http Error: ${errorResponse?.statusCode} -> ${errorResponse?.reasonPhrase}"
            )
        }
    }

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        super.onReceivedSslError(view, handler, error)
        state.setErrorState(
            debugMsg = "SSL Error: ${error?.primaryError} -> ${error?.url}"
        )
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? =
        request?.url?.let { assetLoader?.shouldInterceptRequest(it) }
            ?: super.shouldInterceptRequest(view, request)

    fun setAssetLoader(context: Context) {
        assetLoader =
            WebViewAssetLoader
                .Builder()
                .addPathHandler(ASSETS_PATH, WebViewAssetLoader.AssetsPathHandler(context))
                .build()
    }
}
