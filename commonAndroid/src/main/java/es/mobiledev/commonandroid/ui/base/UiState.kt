package es.mobiledev.commonandroid.ui.base

import es.mobiledev.commonandroid.ui.component.error.UiError

/**
 * Represents the state of the UI.
 *
 * @param T The type of the data held by the UI state.
 * @property data The data to be displayed in the UI.
 * @property isLoading A boolean that indicates if a data load is in progress.
 */
data class UiState<T>(
    val data: T,
    val isLoading: Boolean = false,
    val uiError: UiError = UiError.None,
)
