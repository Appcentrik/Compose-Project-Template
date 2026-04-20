package es.mobiledev.commonandroid.ui.component.error

import es.mobiledev.common.EMPTY_STRING

// enum class UiError {
//    DIALOG, SHEET, SCREEN, SNACK_BAR, EMBEDDED
// }

sealed class UiError(
    open val title: String,
    open val message: String,
    open val action: (() -> Unit)? = null,
) {
    data class Dialog(
        override val title: String,
        override val message: String,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Sheet(
        override val title: String,
        override val message: String,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Screen(
        override val title: String,
        override val message: String,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class SnackBar(
        override val title: String,
        override val message: String,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Embedded(
        override val title: String,
        override val message: String,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data object None : UiError(EMPTY_STRING, EMPTY_STRING)
}
