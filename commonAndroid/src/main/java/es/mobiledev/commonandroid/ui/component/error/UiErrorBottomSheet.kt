package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.bottomSheet.CPTBottomSheet

@Composable
fun UiErrorBottomSheet(
    uiError: UiError.Sheet,
    onDismiss: () -> Unit,
) {
    CPTBottomSheet(onDismiss = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.error_bottom_sheet__padding_start),
                        top = dimensionResource(id = R.dimen.error_bottom_sheet__padding_top),
                        end = dimensionResource(id = R.dimen.error_bottom_sheet__padding_end),
                        bottom = dimensionResource(id = R.dimen.error_bottom_sheet__padding_bottom),
                    ),
        ) {
            UiErrorContent(
                uiError = uiError,
                onDismiss = onDismiss,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        BaseScreen {
            UiErrorBottomSheet(
                uiError =
                    UiError.Sheet(
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
