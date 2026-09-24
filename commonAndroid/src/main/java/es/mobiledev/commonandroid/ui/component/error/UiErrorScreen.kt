package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen

@Composable
fun UiErrorScreen(
    uiError: UiError.Screen,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = BlueGrey50),
    ) {
        UiErrorContent(uiError)
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
                        title = R.string.error_generic_title,
                        message = R.string.error_generic_message,
                        action = {},
                    ),
            )
        }
    }
}
