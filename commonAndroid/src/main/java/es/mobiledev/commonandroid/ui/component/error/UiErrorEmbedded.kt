package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.theme.CPTTheme

@Composable
fun UiErrorEmbedded(
    uiError: UiError.Embedded,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        UiErrorContent(
            uiError = uiError,
            onDismiss = onDismiss,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewUiErrorEmbedded() {
    CPTTheme {
        UiErrorEmbedded(
            uiError =
                UiError.Embedded(
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
