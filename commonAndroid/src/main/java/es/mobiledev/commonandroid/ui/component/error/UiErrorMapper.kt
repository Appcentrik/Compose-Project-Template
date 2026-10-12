package es.mobiledev.commonandroid.ui.component.error

import androidx.annotation.StringRes
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText

/** How a [es.mobiledev.commonandroid.core.error.UiError] has to be rendered. Chosen explicitly by the caller instead of inferred by a type parameter. */
enum class UiErrorPresentation {
    Screen,
    Dialog,
    Sheet,
    SnackBar,
    Embedded,
}

/**
 * Maps an [AppError] to a renderable [es.mobiledev.commonandroid.core.error.UiError].
 *
 * The generic copy is the default: an error always shows text the app owns and translates. Overriding
 * [title] or [message] is what lets an app put something else in front of the user, typically a message
 * the backend sent, and has to be asked for on purpose so server text never reaches the screen by
 * accident.
 *
 * @param presentation how the error is rendered. It is an explicit argument, not a reified type
 *   parameter, so the call site states what the user will see and the compiler rejects an unknown one.
 * @param action what the error button runs, `null` to render a read only error with no button.
 * @param actionLabel label of the action button. Ignored when [action] is null.
 * @param title overrides the generic headline. Keep it null unless the app has its own wording.
 * @param message overrides the generic body. Keep it null unless the app has its own wording.
 */
fun AppError.toUiError(
    presentation: UiErrorPresentation,
    action: (() -> Unit)? = null,
    @StringRes actionLabel: Int = R.string.error_action_retry,
    title: UiErrorText? = null,
    message: UiErrorText? = null,
): UiError {
    val errorAction = action?.let { ErrorAction(actionLabel, it) }
    val genericTitle: UiErrorText
    val genericMessage: UiErrorText
    when (this) {
        is AppError.NetworkError -> {
            genericTitle = UiErrorText.Resource(R.string.error_network_title)
            genericMessage = UiErrorText.Resource(R.string.error_network_message)
        }

        is AppError.ServerError -> {
            genericTitle = UiErrorText.Resource(R.string.error_server_title)
            genericMessage = UiErrorText.Resource(R.string.error_server_message)
        }

        is AppError.TimeoutError -> {
            genericTitle = UiErrorText.Resource(R.string.error_timeout_title)
            genericMessage = UiErrorText.Resource(R.string.error_timeout_message)
        }

        is AppError.UnauthorizedError -> {
            genericTitle = UiErrorText.Resource(R.string.error_unauthorized_title)
            genericMessage = UiErrorText.Resource(R.string.error_unauthorized_message)
        }

        is AppError.NotFoundError -> {
            genericTitle = UiErrorText.Resource(R.string.error_not_found_title)
            genericMessage = UiErrorText.Resource(R.string.error_not_found_message)
        }

        is AppError.ParseError -> {
            genericTitle = UiErrorText.Resource(R.string.error_parse_title)
            genericMessage = UiErrorText.Resource(R.string.error_parse_message)
        }

        is AppError.LocalError -> {
            genericTitle = UiErrorText.Resource(R.string.error_local_title)
            genericMessage = UiErrorText.Resource(R.string.error_local_message)
        }

        is AppError.UnknownError -> {
            genericTitle = UiErrorText.Resource(R.string.error_unknown_title)
            genericMessage = UiErrorText.Resource(R.string.error_unknown_message)
        }
    }

    val errorTitle = title ?: genericTitle
    val errorMessage = message ?: genericMessage

    return when (presentation) {
        UiErrorPresentation.Screen -> UiError.Screen(errorTitle, errorMessage, errorAction)
        UiErrorPresentation.Dialog -> UiError.Dialog(errorTitle, errorMessage, errorAction)
        UiErrorPresentation.Sheet -> UiError.Sheet(errorTitle, errorMessage, errorAction)
        UiErrorPresentation.SnackBar -> UiError.SnackBar(errorTitle, errorMessage, errorAction)
        UiErrorPresentation.Embedded -> UiError.Embedded(errorTitle, errorMessage, errorAction)
    }
}
