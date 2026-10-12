package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.theme.Red0
import es.mobiledev.commonandroid.ui.component.popup.CPTSnackbar

/**
 * Transient error shown on top of the content.
 *
 * @param uiError the error to display.
 * @param modifier the [Modifier] to be applied to this component.
 * @param onAction invoked when the user triggers the action, also dismisses the snackbar.
 * @param onDismiss invoked once the snackbar has finished hiding, always, so the caller can clear
 *   its state. Without it the error would stay in the state after the snackbar is gone.
 */
@Composable
fun UiErrorSnackbar(
    uiError: UiError.SnackBar,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val actionLabel = uiError.actionLabel
    CPTSnackbar(
        title = stringResource(uiError.title),
        message = stringResource(uiError.message),
        actionLabel = actionLabel?.let { label -> stringResource(label) },
        onAction = onAction.takeIf { actionLabel != null },
        onDismiss = onDismiss,
        leadingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                tint = Red0,
                contentDescription = null,
            )
        },
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        UiErrorSnackbar(
            uiError =
                UiError.SnackBar(
                    title = R.string.error_generic_title,
                    message = R.string.error_generic_message,
                    actionLabel = R.string.error_action_retry,
                ),
            onAction = {},
            onDismiss = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewWithoutAction() {
    CPTTheme {
        UiErrorSnackbar(
            uiError =
                UiError.SnackBar(
                    title = R.string.error_generic_title,
                    message = R.string.error_generic_message,
                ),
            onDismiss = {},
        )
    }
}
