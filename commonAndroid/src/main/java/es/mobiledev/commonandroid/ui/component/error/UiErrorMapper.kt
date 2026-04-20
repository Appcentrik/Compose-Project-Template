package es.mobiledev.commonandroid.ui.component.error

import es.mobiledev.common.error.AppError

inline fun <reified E : UiError> AppError.toUiError(
    noinline action: (() -> Unit)? = null,
): UiError {
    val (title, message) =
        when (this) {
            is AppError.NetworkError ->
                "No internet connection" to "Please check your connection and try again."

            else ->
                "Something went wrong" to "We're having trouble loading this content. Please try again."
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
