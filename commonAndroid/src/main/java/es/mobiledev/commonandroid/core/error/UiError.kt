package es.mobiledev.commonandroid.core.error

import androidx.compose.runtime.Immutable

/**
 * Model representing an error state, categorized by its intended UI presentation.
 *
 * @property title The primary headline of the error.
 * @property message Detailed description or recovery suggestion.
 * @property action Optional interaction (e.g., Retry) to resolve the error.
 */
@Immutable
sealed interface UiError {
    val title: UiErrorText
    val message: UiErrorText
    val action: ErrorAction?

    /** True if the error prevents further user interaction until addressed. */
    val isBlocking: Boolean get() = this !is SnackBar

    /** A modal interruption requiring explicit user acknowledgment. */
    data class Dialog(
        override val title: UiErrorText,
        override val message: UiErrorText,
        override val action: ErrorAction? = null,
    ) : UiError

    /** Contextual error displayed within a modal bottom sheet. */
    data class Sheet(
        override val title: UiErrorText,
        override val message: UiErrorText,
        override val action: ErrorAction? = null,
    ) : UiError

    /** A full-screen state replacing the main content (e.g., empty or error states). */
    data class Screen(
        override val title: UiErrorText,
        override val message: UiErrorText,
        override val action: ErrorAction? = null,
    ) : UiError

    /** A brief, non-blocking message at the bottom of the screen. */
    data class SnackBar(
        override val title: UiErrorText,
        override val message: UiErrorText,
        override val action: ErrorAction? = null,
    ) : UiError

    /** An inline error tied to a specific UI component or container. */
    data class Embedded(
        override val title: UiErrorText,
        override val message: UiErrorText,
        override val action: ErrorAction? = null,
    ) : UiError
}
