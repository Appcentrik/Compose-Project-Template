package es.mobiledev.commonandroid.util.extensions

import es.mobiledev.commonandroid.util.webview.ASSETS_PATH
import es.mobiledev.commonandroid.util.webview.APP_ASSETS_URL
import java.net.MalformedURLException
import java.net.URL

const val HTML_REGEX = "</?[a-zA-Z][a-zA-Z0-9]*(\\s+[a-zA-Z_:][-a-zA-Z0-9_:.]*(\\s*=\\s*(\"[^\"]*\"|'[^']*'|[^\\s'\">=]+))?)*\\s*/?>"

fun String.isValidHTML(): Boolean =
    with(Regex(HTML_REGEX)) {
        containsMatchIn(this@isValidHTML)
    }

fun String.isValidUrl(): Boolean =
    try {
        URL(this)
        true
    } catch (_: MalformedURLException) {
        false
    }

fun String.isAssetUrl() = this.contains(ASSETS_PATH)

fun String.toWebViewAssetUrl() =
    when {
        startsWith(APP_ASSETS_URL) -> this
        startsWith(ASSETS_PATH) -> APP_ASSETS_URL + this
        else -> APP_ASSETS_URL + ASSETS_PATH + this
    }
