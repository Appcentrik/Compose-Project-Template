package es.mobiledev.commonandroid.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import es.mobiledev.common.error.AppError
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base [ViewModel] providing state management and error handling.
 *
 * @param T UI state data type.
 * @param appLogger Logger for reporting failures.
 * @param appDispatchers Coroutine dispatchers for launching tasks.
 */
abstract class BaseViewModel<T>(
    private val appLogger: AppLogger,
    protected val appDispatchers: AppDispatchers,
) : ViewModel() {
    /** Mutable UI state flow. */
    protected abstract val mutableUiState: MutableStateFlow<UiState<T>>

    /** Read-only UI state flow. */
    val uiState: StateFlow<UiState<T>> by lazy(LazyThreadSafetyMode.NONE) { mutableUiState.asStateFlow() }

    /** Cached dismiss error callback to prevent unnecessary state updates. */
    private val dismissErrorCallback: () -> Unit = ::dismissError

    /** Updates the data of the UI state, leaving the status untouched. */
    protected fun updateData(block: (T) -> T) {
        mutableUiState.update { currentUiState ->
            currentUiState.copy(data = block(currentUiState.data))
        }
    }

    /** Updates the data and stops loading, leaving the status untouched. */
    protected fun showSuccess(block: (T) -> T) {
        mutableUiState.update { currentUiState ->
            currentUiState.copy(
                data = block(currentUiState.data),
                status = currentUiState.status.copy(isLoading = false),
            )
        }
    }

    /** Starts a fresh load, which supersedes any error currently on screen. */
    protected fun setLoading() {
        mutableUiState.update { currentUiState ->
            currentUiState.copy(status = UiStatus(isLoading = true))
        }
    }

    /** Displays [uiError] in the appropriate channel (blocking or snackbar). */
    protected fun showError(uiError: UiError) {
        mutableUiState.update { currentUiState ->
            currentUiState.copy(
                status =
                    currentUiState.status.copy(
                        isLoading = false,
                        blockingError = uiError.takeIf { it.isBlocking },
                        snackbarError = uiError.takeIf { !it.isBlocking },
                        onErrorDismiss = dismissErrorCallback,
                    ),
            )
        }
    }

    /** Displays [error] as a snackbar with an action and logs it. */
    protected fun showSnackBarError(
        error: AppError,
        action: () -> Unit,
        @StringRes actionLabel: Int = R.string.error_action_retry,
    ) {
        showError(
            error.toUiError(
                UiErrorPresentation.SnackBar,
                action = action,
                actionLabel = actionLabel,
            ),
        )
        logAppError(error)
    }

    /** Runs the action of the current error and dismisses it. */
    fun onErrorAction() {
        val error = uiState.value.status.blockingError ?: uiState.value.status.snackbarError
        dismissError()
        error?.action?.onClick()
    }

    /** Dismisses the current error. */
    fun dismissError() {
        mutableUiState.update { currentUiState ->
            currentUiState.copy(
                status =
                    currentUiState.status.copy(
                        blockingError = null,
                        snackbarError = null,
                    ),
            )
        }
    }

    /** Logs an [AppError] using the class name as the tag. */
    protected fun logAppError(error: AppError) = appLogger.logAppError(this::class.java.simpleName, error)
}
