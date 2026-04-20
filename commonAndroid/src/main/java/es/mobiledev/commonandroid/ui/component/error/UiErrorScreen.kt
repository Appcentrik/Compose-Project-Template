package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
private fun Preview() {
    CPTTheme {
        BaseScreen {
            UiErrorScreen(
                uiError =
                    UiError.Screen(
                        title = "Oops, it looks like there was a problem",
                        message = "An unexpected error occurred. Please try again later.",
                        action = {},
                    ),
            )
        }
    }
}
