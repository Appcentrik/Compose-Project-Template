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

@Composable
fun UiErrorSnackbar(
    uiError: UiError.SnackBar,
    modifier: Modifier = Modifier,
) {
    CPTSnackbar(
        title = stringResource(uiError.title),
        message = stringResource(uiError.message),
        actionLabel = stringResource(R.string.error_action_confirm),
        action = uiError.action,
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
                    action = {},
                ),
        )
    }
}
