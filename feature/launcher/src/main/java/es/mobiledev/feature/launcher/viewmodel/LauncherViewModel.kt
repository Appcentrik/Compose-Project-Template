package es.mobiledev.feature.launcher.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.feature.launcher.state.LauncherUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel
    @Inject
    constructor(
        appLogger: AppLogger,
        appDispatchers: AppDispatchers,
    ) : BaseViewModel<LauncherUiState>(appLogger, appDispatchers) {
        override val mutableUiState: MutableStateFlow<UiState<LauncherUiState>> =
            MutableStateFlow(value = UiState(data = LauncherUiState()))

        init {
            viewModelScope.launch {
                doSomething()
            }
        }

        private suspend fun doSomething() {
            delay(3000L)
            showSuccess { currentUiState ->
                currentUiState.copy(
                    hasFinish = true
                )
            }
        }
    }
