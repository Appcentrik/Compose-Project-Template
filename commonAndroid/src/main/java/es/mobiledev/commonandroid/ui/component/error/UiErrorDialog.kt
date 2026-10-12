package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.dialog.CPTDialog

@Composable
fun UiErrorDialog(
    uiError: UiError.Dialog,
    onDismiss: () -> Unit,
) {
    CPTDialog(
        onDismissRequest = onDismiss,
        showCloseButton = true,
    ) {
        UiErrorContent(
            uiError = uiError,
            onDismiss = onDismiss,
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        BaseScreen {
            UiErrorDialog(
                uiError =
                    UiError.Dialog(
                        title = UiErrorText.Resource(id = R.string.error_generic_title),
                        message = UiErrorText.Resource(id = R.string.error_generic_message),
                        action =
                            ErrorAction(
                                label = R.string.error_action_retry,
                                onClick = {},
                            ),
                    ),
                onDismiss = {},
            )
        }
    }
}
