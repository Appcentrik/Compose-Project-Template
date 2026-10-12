package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.core.error.resolve
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.theme.Red0
import es.mobiledev.commonandroid.ui.component.popup.CPTSnackbar

/**
 * @param uiError the error to display.
 * @param modifier the [Modifier] to be applied to this component.
 * @param onDismiss invoked once the snackbar has finished hiding.
 */
@Composable
fun UiErrorSnackbar(
    uiError: UiError.SnackBar,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {
    val action = uiError.action
    CPTSnackbar(
        title = uiError.title.resolve(),
        message = uiError.message.resolve(),
        actionLabel = action?.let { stringResource(it.label) },
        onAction = action?.onClick,
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
                    title = UiErrorText.Resource(R.string.error_generic_title),
                    message = UiErrorText.Resource(R.string.error_generic_message),
                    action = ErrorAction(label = R.string.error_action_retry, onClick = {}),
                ),
            onDismiss = {},
        )
    }
}
