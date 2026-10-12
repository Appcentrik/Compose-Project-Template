package es.mobiledev.commonandroid.ui.base

import androidx.lifecycle.ViewModel
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * CPT implementation of a base [ViewModel].
 *
 * This class provides a basic structure for a [ViewModel] in the application. It manages the UI state
 * using a [MutableStateFlow] and provides helper functions to update it.
 *
 * Error handling contract:
 * - Only [showError] and [showSnackBarError] write to the error channels, so a successful operation
 *   never wipes an error raised by a concurrent one.
 * - The action of an error is kept here, in [pendingAction], instead of inside [UiError], so the state
 *   stays a comparable value and the screen stays skippable.
 * - [pendingAction] always belongs to the error currently visible. Every method that hides an error
 *   clears it, which is what keeps the pairing meaningful.
 *
 * @param T The type of the UI state data.
 */
abstract class BaseViewModel<T>(
    private val appLogger: AppLogger,
) : ViewModel() {
    /**
     * The mutable state flow that holds the current UI state.
     * It is protected to be accessible only by subclasses.
     */
    protected abstract val mutableUiState: MutableStateFlow<UiState<T>>

    /**
     * The current UI state, read only.
     *
     * Resolved lazily on purpose: reading [mutableUiState] during construction would return null,
     * because a subclass property is not initialised until after super() returns.
     */
    val uiState: StateFlow<UiState<T>> by lazy(LazyThreadSafetyMode.NONE) { mutableUiState.asStateFlow() }

    private var pendingAction: (() -> Unit)? = null

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
        pendingAction = null
        mutableUiState.update { currentUiState ->
            currentUiState.copy(status = UiStatus(isLoading = true))
        }
    }

    /**
     * Shows [uiError] in the channel matching its presentation, replacing whatever was shown before.
     *
     * When [action] is null the error loses its action label, so it can never render a button with
     * no behaviour behind it, and a snackbar with no action is free to dismiss itself.
     *
     * @param uiError the error to display.
     * @param action action bound to the error button, `null` for a read only error.
     */
    protected fun showError(
        uiError: UiError,
        action: (() -> Unit)? = null,
    ) {
        pendingAction = action
        val error = if (action == null) uiError.withoutAction() else uiError
        mutableUiState.update { currentUiState ->
            currentUiState.copy(
                status =
                    currentUiState.status.copy(
                        isLoading = false,
                        blockingError = error.takeIf { it.isBlocking },
                        snackbarError = error.takeIf { !it.isBlocking },
                    ),
            )
        }
    }

    /**
     * Shows [error] as a transient snackbar with an action, and logs it. The usual entry point for a
     * failure the user can recover from without leaving the screen.
     *
     * @param action what the error button does. Any behaviour is valid, not only a repeated request.
     */
    protected fun showSnackBarError(
        error: AppError,
        action: () -> Unit,
    ) {
        showError(
            uiError = error.toUiError(UiErrorPresentation.SnackBar),
            action = action,
        )
        logAppError(error)
    }

    /** Runs the action bound to the error currently on screen and dismisses it. */
    fun onErrorAction() {
        val action = pendingAction
        dismissError()
        action?.invoke()
    }

    /** Dismisses the error currently on screen without running any action. */
    fun dismissError() {
        pendingAction = null
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

    /**
     * Logs an [AppError] using the simple name of the current class as the tag.
     *
     * @param error The [AppError] to be logged.
     */
    protected fun logAppError(error: AppError) = appLogger.logAppError(this::class.java.simpleName, error)
}
