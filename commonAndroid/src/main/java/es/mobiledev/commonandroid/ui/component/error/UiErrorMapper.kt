package es.mobiledev.commonandroid.ui.component.error

import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R

inline fun <reified E : UiError> AppError.toUiError(
    noinline action: (() -> Unit)? = null,
): UiError {
    val (title, message) =
        when (this) {
            is AppError.NetworkError ->
                R.string.error_network_title to R.string.error_network_message

            else ->
                R.string.error_generic_title to R.string.error_generic_message
        }

    return when (E::class) {
        UiError.Dialog::class -> UiError.Dialog(title, message, action)
        UiError.Sheet::class -> UiError.Sheet(title, message, action)
        UiError.Screen::class -> UiError.Screen(title, message, action)
        UiError.SnackBar::class -> UiError.SnackBar(title, message, action)
        UiError.Embedded::class -> UiError.Embedded(title, message, action)
        else -> UiError.None
    }
}
