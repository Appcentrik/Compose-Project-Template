package es.mobiledev.cpt.ui.screen.testNavigation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.base.BaseViewModel
import es.mobiledev.commonandroid.ui.base.UiState
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Drives [BaseViewModel] through its real error API so [TestScreen] exercises the production path
 * instead of hand built [UiError] values.
 */
@HiltViewModel
class TestViewModel
    @Inject
    constructor(
        appLogger: AppLogger
    ) : BaseViewModel<TestUiState>(appLogger) {
        override val mutableUiState: MutableStateFlow<UiState<TestUiState>> =
            MutableStateFlow(value = UiState(data = TestUiState()))

        fun onShowError(
            error: AppError,
            presentation: UiErrorPresentation,
        ) {
            showError(
                uiError = error.toUiError(presentation),
                action = ::countAction,
            )
            updateData { it.copy(lastAction = "showError ${presentation.name}") }
        }

        fun onShowTransientError(
            error: AppError,
            withAction: Boolean,
        ) {
            countSnackBar()
            if (withAction) {
                showSnackBarError(error = error, action = ::countAction)
            } else {
                showError(
                    uiError = error.toUiError(UiErrorPresentation.SnackBar),
                    action = null,
                )
            }
            updateData { it.copy(lastAction = "snackbar withAction=$withAction") }
        }

        /**
         * Shows one snackbar, dismisses it and shows another one. Before the dismissal notification
         * was added, the second snackbar stayed invisible because the transition state was never
         * reset.
         */
        fun onShowTransientErrorsInSequence() {
            viewModelScope.launch {
                showSnackBarError(
                    error = AppError.NetworkError("first"),
                    action = ::countAction,
                )
                countSnackBar()
                delay(timeMillis = 900)

                dismissError()

                showSnackBarError(
                    error = AppError.ServerError(code = 500, message = "second"),
                    action = ::countAction,
                )
                countSnackBar()
            }
            updateData { it.copy(lastAction = "two snackbars in a row") }
        }

        /**
         * A success must never clear an error raised by another operation: this is the case where
         * the favourites observer used to wipe the article error.
         */
        fun onSuccessKeepingTheError() {
            showSuccess { it.copy(lastAction = "success, error untouched") }
        }

        fun onStartLoading() {
            setLoading()
            updateData { it.copy(lastAction = "loading") }
        }

        fun onDismiss() {
            dismissError()
            updateData { it.copy(lastAction = "dismissed") }
        }

        fun onDismissWithAction() {
            onErrorAction()
        }

        fun onResetCounters() {
            updateData { it.copy(actionInvocations = 0, snackBarsShown = 0) }
        }

        fun onNavigationSmokeTest() {
            viewModelScope.launch {
                updateData { it.copy(lastAction = "navigating...") }
                delay(timeMillis = 1200)
                setLoading()
                delay(timeMillis = 1200)
                showSuccess { it.copy(lastAction = "navigation works") }
            }
        }

        private fun countAction() {
            updateData { it.copy(actionInvocations = it.actionInvocations + 1) }
        }

        private fun countSnackBar() {
            updateData { it.copy(snackBarsShown = it.snackBarsShown + 1) }
        }
    }
