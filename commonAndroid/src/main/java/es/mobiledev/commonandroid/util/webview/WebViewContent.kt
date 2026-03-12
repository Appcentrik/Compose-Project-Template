package es.mobiledev.commonandroid.util.webview

import es.mobiledev.commonandroid.util.extensions.isAssetUrl
import es.mobiledev.commonandroid.util.extensions.isValidHTML
import es.mobiledev.commonandroid.util.extensions.isValidUrl

/**
 * CPT design utility
 *
 * WebViewContent represents the different types of content that can be loaded into a WebView.
 */
sealed class WebViewContent {
    /**
     * Represents a remote URL.
     * @param url the web address to load
     */
    data class Url(
        val url: String
    ) : WebViewContent()

    /**
     * Represents raw HTML data.
     * @param data the HTML string to be rendered
     */
    data class Html(
        val data: String
    ) : WebViewContent()

    /**
     * Represents a local asset.
     * @param url the path to the asset file
     */
    data class Asset(
        val url: String
    ) : WebViewContent()

    /**
     * Represents an unknown or unsupported content type.
     */
    data object Unknown : WebViewContent()
}

/**
 * CPT design utility
 *
 * Converts a [String] to a [WebViewContent] using validation extensions.
 *
 * - [isAssetUrl] -> [WebViewContent.Asset]
 * - [isValidUrl] -> [WebViewContent.Url]
 * - [isValidHTML] -> [WebViewContent.Html]
 * - Otherwise -> [WebViewContent.Unknown]
 *
 * @return the corresponding [WebViewContent]
 */
fun String.toWebViewContent(): WebViewContent =
    when {
        isAssetUrl() -> WebViewContent.Asset(this)
        isValidUrl() -> WebViewContent.Url(this)
        isValidHTML() -> WebViewContent.Html(this)
        else -> WebViewContent.Unknown
    }
