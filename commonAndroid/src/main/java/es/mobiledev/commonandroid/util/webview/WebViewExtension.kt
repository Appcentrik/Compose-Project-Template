package es.mobiledev.commonandroid.util.webview

import android.webkit.WebView
import es.mobiledev.commonandroid.util.extensions.toWebViewAssetUrl

/**
 * CPT design utility
 *
 * Loads raw HTML data into the WebView.
 *
 * This extension function simplifies loading HTML strings by providing default values
 * for base URL, MIME type, and encoding.
 *
 * @param data the HTML string to be loaded
 */
fun WebView.loadHtml(data: String) {
    loadDataWithBaseURL(
        null,
        data,
        "text/html",
        "UTF-8",
        null
    )
}

/**
 * CPT design utility
 *
 * Loads a local asset file into the WebView using a secure internal URL.
 *
 * This function uses the [toWebViewAssetUrl] extension to resolve the asset path
 * and works in conjunction with [androidx.webkit.WebViewAssetLoader] to safely
 * load files from the app's assets folder.
 *
 * @param url the path to the asset (e.g., "asset://web/index.html" or "asset://js/script.js")
 */
fun WebView.loadAsset(url: String) {
    loadUrl(url.toWebViewAssetUrl())
}
