package es.mobiledev.commonandroid.util.extensions

import es.mobiledev.common.HTML_REGEX
import es.mobiledev.commonandroid.util.webview.APP_ASSETS_URL
import es.mobiledev.commonandroid.util.webview.ASSETS_PATH
import es.mobiledev.commonandroid.util.webview.BODY_TAG
import es.mobiledev.commonandroid.util.webview.BODY_TAG_CLOSURE
import es.mobiledev.commonandroid.util.webview.BODY_TAG_START
import es.mobiledev.commonandroid.util.webview.HEAD_TAG
import es.mobiledev.commonandroid.util.webview.HEAD_TAG_CLOSURE
import es.mobiledev.commonandroid.util.webview.HTML_TAG
import es.mobiledev.commonandroid.util.webview.HTML_TAG_CLOSURE
import es.mobiledev.commonandroid.util.webview.HTML_TAG_START
import es.mobiledev.commonandroid.util.webview.META_VIEWPORT
import es.mobiledev.commonandroid.util.webview.STYLE_IMG
import es.mobiledev.commonandroid.util.webview.STYLE_TAG_START
import es.mobiledev.commonandroid.util.webview.VIEWPORT
import java.net.MalformedURLException
import java.net.URL

fun String.isValidHTML(): Boolean =
    Regex(HTML_REGEX)
        .containsMatchIn(this)

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

fun String.prepareHtml(): String =
    when {
        contains(HTML_TAG_START, ignoreCase = true) -> this
        contains(BODY_TAG_START, ignoreCase = true) -> "$HTML_TAG$this$HTML_TAG_CLOSURE"
        else -> "$HTML_TAG$BODY_TAG$this$BODY_TAG_CLOSURE$HTML_TAG_CLOSURE"
    }.injectHtmlFixes()

private fun String.injectHtmlFixes(): String =
    run {
        when {
            contains(VIEWPORT, ignoreCase = true) -> this
            contains(HEAD_TAG, ignoreCase = true) ->
                replace(
                    oldValue = HEAD_TAG,
                    newValue = "$HEAD_TAG$META_VIEWPORT",
                    ignoreCase = true,
                )

            else ->
                replace(
                    oldValue = BODY_TAG_START,
                    newValue = "$HEAD_TAG$META_VIEWPORT$HEAD_TAG_CLOSURE$BODY_TAG_START",
                    ignoreCase = true,
                )
        }
    }.run {
        when {
            contains(STYLE_TAG_START, ignoreCase = true) -> this
            contains(HEAD_TAG_CLOSURE, ignoreCase = true) ->
                replace(
                    oldValue = HEAD_TAG_CLOSURE,
                    newValue = "$STYLE_IMG$HEAD_TAG_CLOSURE",
                    ignoreCase = true,
                )
            else ->
                replace(
                    oldValue = BODY_TAG_START,
                    newValue = "$STYLE_IMG$BODY_TAG_START",
                    ignoreCase = true,
                )
        }
    }
