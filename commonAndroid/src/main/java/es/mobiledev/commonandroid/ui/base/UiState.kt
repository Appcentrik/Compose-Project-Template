package es.mobiledev.commonandroid.ui.base

import androidx.compose.runtime.Immutable
import es.mobiledev.commonandroid.core.error.UiError

/**
 * Represents screen overlay elements (loading state, errors, chrome) separate from data.
 *
 * @property isLoading Whether a data load is in progress.
 * @property blockingError Error that replaces screen content.
 * @property snackbarError Transient error shown over content.
 * @property onErrorDismiss Callback to dismiss the current error.
 */
@Immutable
data class UiStatus(
    val isLoading: Boolean = false,
    val blockingError: UiError? = null,
    val snackbarError: UiError? = null,
    val onErrorDismiss: () -> Unit = {},
)

/**
 * Represents the complete UI state.
 *
 * @param T Data type held by the UI state.
 * @property data Data to be displayed in the UI.
 * @property status Screen overlays and status (loading, errors).
 */
@Immutable
data class UiState<T>(
    val data: T,
    val status: UiStatus = UiStatus(),
)
