package es.mobiledev.commonandroid.ui.base

import androidx.compose.runtime.Immutable
import es.mobiledev.commonandroid.ui.component.error.UiError

/**
 * Everything [BaseScreen] draws on top of the screen content, kept apart from the data on purpose.
 *
 * It is a separate immutable value so [BaseScreen] stays skippable: a screen rebinding its data
 * never invalidates this object, and therefore never recomposes the chrome.
 *
 * @property isLoading a flag that indicates if a data load is in progress.
 * @property blockingError an error that replaces the content of the screen. Mutually exclusive with
 *   [isLoading] by contract.
 * @property snackbarError a transient error shown on top of the content, independent of both.
 */
@Immutable
data class UiStatus(
    val isLoading: Boolean = false,
    val blockingError: UiError? = null,
    val snackbarError: UiError? = null,
)

/**
 * Represents the state of the UI.
 *
 * Errors live in two independent channels on purpose. A screen runs several operations at the same
 * time, so a single shared slot made concurrent producers overwrite each other: a successful request
 * could silently wipe the error raised by another one.
 *
 * @param T The type of the data held by the UI state.
 * @property data The data to be displayed in the UI.
 * @property status What the screen shows on top of [data].
 */
@Immutable
data class UiState<T>(
    val data: T,
    val status: UiStatus = UiStatus(),
)
