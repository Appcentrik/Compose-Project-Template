package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.BlueGrey800
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen

@Composable
fun UiErrorDialog(
    uiError: UiError.Dialog,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
    ) {
        Card(
            colors =
                CardDefaults.cardColors(
                    containerColor = BlueGrey50,
                ),
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier =
                        Modifier
                            .align(Alignment.End)
                            .size(16.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        tint = BlueGrey800,
                        contentDescription = null,
                    )
                }
                UiErrorContent(
                    uiError = uiError,
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CPTTheme {
        BaseScreen {
            UiErrorDialog(
                uiError =
                    UiError.Dialog(
                        title = "Oops, it looks like there was a problem",
                        message = "An unexpected error occurred. Please try again later.",
                        action = {},
                    ),
                onDismiss = {},
            )
        }
    }
}
