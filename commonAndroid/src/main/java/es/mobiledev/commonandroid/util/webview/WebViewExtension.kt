package es.mobiledev.commonandroid.util.webview

import android.webkit.WebView
import es.mobiledev.common.ENCODING_UTF8
import es.mobiledev.common.MIME_TYPE_HTML
import es.mobiledev.commonandroid.util.extensions.prepareHtml
import es.mobiledev.commonandroid.util.extensions.toWebViewAssetUrl

/**
 * CPT design utility
 *
 * Loads an HTML string into the WebView, automatically injecting the necessary
 * HTML structure (viewport meta tag, image styles, etc.) if not already present.
 *
 * @param data the HTML string to be loaded, with or without full HTML structure
 * @see String.prepareHtml
 */
fun WebView.loadHtml(data: String) {
    loadDataWithBaseURL(
        null,
        data.prepareHtml(),
        MIME_TYPE_HTML,
        ENCODING_UTF8,
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
