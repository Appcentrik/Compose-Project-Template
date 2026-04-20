package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import es.mobiledev.commonandroid.theme.BlueGrey400
import es.mobiledev.commonandroid.theme.BlueGrey50
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiErrorBottomSheet(
    uiError: UiError.Sheet,
    onDismiss: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        ModalBottomSheet(
            containerColor = BlueGrey50,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = BlueGrey400,
                )
            },
            onDismissRequest = {
                coroutineScope
                    .launch(Dispatchers.Main) {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        onDismiss()
                    }
            },
        ) {
            UiErrorContent(
                uiError = uiError,
                modifier =
                    Modifier
                        .padding(all = 16.dp),
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
                        title = "Oops, it looks like there was a problem",
                        message = "An unexpected error occurred. Please try again later.",
                        action = {},
                    ),
                onDismiss = {},
            )
        }
    }
}
