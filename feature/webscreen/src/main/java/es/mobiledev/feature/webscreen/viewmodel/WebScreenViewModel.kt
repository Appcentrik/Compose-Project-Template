package es.mobiledev.feature.webscreen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.util.webview.client.CPTWebViewClient
import es.mobiledev.commonandroid.util.webview.state.CPTWebViewClientListener
import es.mobiledev.commonandroid.util.webview.toWebViewContent
import es.mobiledev.feature.webscreen.state.WebScreenUiState
import es.mobiledev.navigation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<WebScreenUiState>(),
        CPTWebViewClientListener {
        val args = savedStateHandle.toRoute<AppScreens.WebScreen>()
        override val uiState: MutableStateFlow<UiState<WebScreenUiState>> =
            MutableStateFlow(UiState(WebScreenUiState()))

        init {
            viewModelScope.launch {
                initData()
            }
        }

        private fun initData() {
            uiState.loadingState()
            uiState.updateState { currentUiState ->
                currentUiState.copy(
                    client =
                        object : CPTWebViewClient() {}.apply {
                            setListener(this@WebScreenViewModel)
                        }
                )
            }
            uiState.successState { currentState ->
                currentState.copy(
                    content = args.url.toWebViewContent()
                )
            }
        }

        override fun onLoading() {
            uiState.loadingState()
        }

        override fun onFinish() {
            uiState.successState {
                it
            }
        }

        override fun onError(errorMsg: String) {
            TODO("Not yet implemented")
        }

        override fun onCanGoBack(canGoBack: Boolean) {
            uiState.updateState { currentUiState ->
                currentUiState.copy(
                    canGoBack = canGoBack
                )
            }
        }
    }
