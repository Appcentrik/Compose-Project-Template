package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.CPTTheme

@Composable
fun UiErrorEmbedded(
    uiError: UiError.Embedded,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = BlueGrey50,
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            UiErrorContent(uiError)
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewUiErrorEmbedded() {
    CPTTheme {
        UiErrorEmbedded(
            uiError =
                UiError.Embedded(
                    title = R.string.error_preview_title,
                    message = R.string.error_preview_message,
                    action = {},
                ),
        )
    }
}
