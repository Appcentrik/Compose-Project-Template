package es.mobiledev.commonandroid.ui.component.error

import androidx.annotation.StringRes
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R

/** How a [UiError] has to be rendered. Chosen explicitly by the caller instead of inferred by a type parameter. */
enum class UiErrorPresentation {
    Screen,
    Dialog,
    Sheet,
    SnackBar,
    Embedded,
}

/**
 * Maps an [AppError] to the [UiError] the UI can render.
 *
 * @param presentation how the error has to be shown. It is an explicit argument, not a reified type
 *   parameter, so the call site states what the user will see and the compiler rejects an unknown one.
 * @param actionLabel label of the action button, `null` to render no action.
 */
fun AppError.toUiError(
    presentation: UiErrorPresentation,
    @StringRes actionLabel: Int? = R.string.error_action_retry,
): UiError {
    val title: Int
    val message: Int
    when (this) {
        is AppError.NetworkError -> {
            title = R.string.error_network_title
            message = R.string.error_network_message
        }

        is AppError.ServerError -> {
            title = R.string.error_server_title
            message = R.string.error_server_message
        }

        is AppError.TimeoutError -> {
            title = R.string.error_timeout_title
            message = R.string.error_timeout_message
        }

        is AppError.UnauthorizedError -> {
            title = R.string.error_unauthorized_title
            message = R.string.error_unauthorized_message
        }

        is AppError.NotFoundError -> {
            title = R.string.error_not_found_title
            message = R.string.error_not_found_message
        }

        is AppError.ParseError -> {
            title = R.string.error_parse_title
            message = R.string.error_parse_message
        }

        is AppError.LocalError -> {
            title = R.string.error_local_title
            message = R.string.error_local_message
        }

        is AppError.UnknownError -> {
            title = R.string.error_unknown_title
            message = R.string.error_unknown_message
        }
    }

    return when (presentation) {
        UiErrorPresentation.Screen -> UiError.Screen(title, message, actionLabel)
        UiErrorPresentation.Dialog -> UiError.Dialog(title, message, actionLabel)
        UiErrorPresentation.Sheet -> UiError.Sheet(title, message, actionLabel)
        UiErrorPresentation.SnackBar -> UiError.SnackBar(title, message, actionLabel)
        UiErrorPresentation.Embedded -> UiError.Embedded(title, message, actionLabel)
    }
}
