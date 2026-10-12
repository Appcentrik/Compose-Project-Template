package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen

@Composable
fun UiErrorScreen(
    uiError: UiError.Screen,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxSize()
                .background(color = BlueGrey50),
    ) {
        UiErrorContent(
            uiError = uiError,
            modifier =
                Modifier.widthIn(
                    max = dimensionResource(id = R.dimen.error_content__max_width),
                ),
            onDismiss = onDismiss,
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        BaseScreen {
            UiErrorScreen(
                uiError =
                    UiError.Screen(
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
