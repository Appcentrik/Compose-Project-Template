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
import androidx.webkit.WebViewAssetLoader
import es.mobiledev.commonandroid.util.webview.ASSETS_PATH
import es.mobiledev.commonandroid.util.webview.state.CPTWebViewClientListener

/**
 * CPT design utility
 *
 * CPTWebViewClient is a custom [WebViewClient] that tracks the loading state of a WebView.
 *
 * This class integrates with [CPTWebViewClientListener] to notify about page loading events,
 * errors, and navigation history updates. It also supports local asset loading via [WebViewAssetLoader].
 *
 * @param assetLoader an optional [WebViewAssetLoader] for handling local asset requests.
 * If null, local asset interception is disabled.
 *
 * @see WebViewClient
 * @see CPTWebViewClientListener
 * @see WebViewAssetLoader
 */
abstract class CPTWebViewClient(
    internal var assetLoader: WebViewAssetLoader? = null
) : WebViewClient() {
    private var listener: CPTWebViewClientListener? = null

    override fun onPageStarted(
        view: WebView?,
        url: String?,
        favicon: Bitmap?
    ) {
        super.onPageStarted(view, url, favicon)
        listener?.onLoading()
    }

    override fun onPageFinished(
        view: WebView?,
        url: String?
    ) {
        super.onPageFinished(view, url)
        listener?.onFinish()
    }

    override fun doUpdateVisitedHistory(
        view: WebView?,
        url: String?,
        isReload: Boolean
    ) {
        super.doUpdateVisitedHistory(view, url, isReload)
        listener?.onCanGoBack(view?.canGoBack() == true)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        if (request?.isForMainFrame == true) {
            listener?.onError("Error: ${error?.errorCode} -> ${error?.description}")
        }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        if (request?.isForMainFrame == true) {
            listener?.onError("Http Error: ${errorResponse?.statusCode} -> ${errorResponse?.reasonPhrase}")
        }
    }

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        super.onReceivedSslError(view, handler, error)
        listener?.onError("SSL Error: ${error?.primaryError} -> ${error?.url}")
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? =
        request?.url?.let { assetLoader?.shouldInterceptRequest(it) }
            ?: super.shouldInterceptRequest(view, request)

    fun setListener(listener: CPTWebViewClientListener) {
        this.listener = listener
    }

    fun setAssetLoader(context: Context) {
        assetLoader =
            WebViewAssetLoader
                .Builder()
                .addPathHandler(ASSETS_PATH, WebViewAssetLoader.AssetsPathHandler(context))
                .build()
    }
}
