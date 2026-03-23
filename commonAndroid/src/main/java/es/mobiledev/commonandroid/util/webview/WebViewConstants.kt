package es.mobiledev.commonandroid.util.webview

// region DATA LOAD
const val APP_ASSETS_URL = "https://appassets.androidplatform.net"
const val ASSETS_PATH = "/assets/"
const val INDEX_HTML_PATH = "${ASSETS_PATH}web/index.html"

// endregion

// region HTML LOAD
const val HEAD_TAG = "<head>"
const val HEAD_TAG_CLOSURE = "</head>"
const val HTML_TAG_START = "<html"
const val HTML_TAG = "<html>"
const val HTML_TAG_CLOSURE = "</html>"
const val BODY_TAG_START = "<body"
const val BODY_TAG = "<body>"
const val BODY_TAG_CLOSURE = "</body>"
const val VIEWPORT = "viewport"
const val META_VIEWPORT = """<meta name=$VIEWPORT content="width=device-width, initial-scale=1.0">"""
const val STYLE_TAG_START = "<style"
const val STYLE_IMG = "<style>body { background: transparent; } img { max-width: 100%; height: auto; }</style>"

// endregion
