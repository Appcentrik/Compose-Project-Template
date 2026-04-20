package es.mobiledev.commonandroid.ui.base

import android.util.Log
import androidx.lifecycle.ViewModel
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.ui.component.error.UiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * CPT implementation of a base [ViewModel].
 *
 * This class provides a basic structure for a [ViewModel] in the application. It manages the UI state
 * using a [MutableStateFlow] and provides helper functions to update the state.
 *
 * @param T The type of the UI state data.
 */
abstract class BaseViewModel<T> : ViewModel() {
    /**
     * The mutable state flow that holds the current UI state.
     * It is protected to be accessible only by subclasses.
     */
    protected abstract val uiState: MutableStateFlow<UiState<T>>

    /**
     * Returns the UI state as a read-only [StateFlow].
     *
     * @return A [StateFlow] of the current [UiState].
     */
    fun getUiState(): StateFlow<UiState<T>> = uiState.asStateFlow()

    /**
     * Updates the data within the current UI state.
     *
     * @param block A lambda function that takes the current data and returns the updated data.
     */
    fun MutableStateFlow<UiState<T>>.updateState(block: (T) -> T) {
        update { currentUiState ->
            currentUiState.copy(data = block(currentUiState.data))
        }
    }

    /**
     * Sets the UI state to loading.
     */
    fun MutableStateFlow<UiState<T>>.loadingState() {
        update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
                uiError = UiError.None,
            )
        }
    }

    /**
     * Sets the UI state to success and updates the data.
     *
     * @param block A lambda function that takes the current data and returns the updated data.
     */
    fun MutableStateFlow<UiState<T>>.successState(block: (T) -> T) {
        update { currentUiState ->
            currentUiState.copy(
                data = block(currentUiState.data),
                isLoading = false,
                uiError = UiError.None,
            )
        }
    }

    /**
     * Sets the UI state to an error state and stops loading.
     *
     * @param uiError The [UiError] to be displayed in the UI.
     */
    fun MutableStateFlow<UiState<T>>.errorState(uiError: UiError) {
        update { currentUiState ->
            currentUiState.copy(
                uiError = uiError,
                isLoading = false,
            )
        }
    }

    /**
     * Logs an [AppError] using the simple name of the current class as the tag.
     *
     * @param error The [AppError] to be logged.
     */
    fun ViewModel.logAppError(error: AppError) = Log.e(this::class.java.simpleName, error.message, error.throwable)
}
