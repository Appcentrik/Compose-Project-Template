package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey400
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiErrorBottomSheet(
    uiError: UiError.Sheet,
    onDismiss: () -> Unit,
    onAction: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        containerColor = BlueGrey50,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = BlueGrey400)
        },
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope
                .launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                }
        },
    ) {
        UiErrorContent(
            title = uiError.title,
            message = uiError.message,
            actionLabel = uiError.actionLabel,
            onAction = onAction.takeIf { uiError.actionLabel != null },
            modifier =
                Modifier
                    .padding(all = dimensionResource(R.dimen.error_dialog__padding)),
        )
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
                        title = R.string.error_generic_title,
                        message = R.string.error_generic_message,
                        actionLabel = R.string.error_action_retry,
                    ),
                onDismiss = {},
                onAction = {},
            )
        }
    }
}
