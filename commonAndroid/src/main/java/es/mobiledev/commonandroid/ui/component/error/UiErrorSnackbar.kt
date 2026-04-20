package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.Red0
import es.mobiledev.commonandroid.ui.component.popup.CPTSnackbar

@Composable
fun UiErrorSnackbar(
    uiError: UiError.SnackBar,
    modifier: Modifier = Modifier,
) {
    CPTSnackbar(
        title = uiError.title,
        message = uiError.message,
        actionLabel = "Confirm",
        // action = uiError.action,
        leadingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                tint = Red0,
                contentDescription = null,
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun Preview() {
    UiErrorSnackbar(
        uiError =
            UiError.SnackBar(
                title = "Oops, it looks like there was a problem",
                message = "An unexpected error occurred. Please try again later.",
                action = {},
            ),
    )
}
