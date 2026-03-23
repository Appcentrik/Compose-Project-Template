package es.mobiledev.commonandroid.util.webview.state

interface CPTWebViewClientListener {
    fun onLoading()

    fun onFinish()

    fun onError(errorMsg: String)

    fun onCanGoBack(canGoBack: Boolean)
}
