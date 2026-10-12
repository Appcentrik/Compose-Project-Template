package es.mobiledev.commonandroid.ui.component.bottomSheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import es.mobiledev.commonandroid.theme.CptDragHandle
import es.mobiledev.commonandroid.theme.CptSurfaceElevated
import es.mobiledev.commonandroid.theme.getBottomSheetShape
import kotlinx.coroutines.launch

/**
 * Common modal bottom sheet shell.
 *
 * Handles the sheet surface, shape, drag handle and the hide animation before reporting dismissal.
 *
 * @param onDismiss invoked once the sheet has finished hiding.
 * @param modifier the [Modifier] to be applied to the sheet.
 * @param content the content of the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPTBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope
                .launch { sheetState.hide() }
                .invokeOnCompletion { onDismiss() }
        },
        modifier = modifier,
        sheetState = sheetState,
        shape = getBottomSheetShape(),
        containerColor = CptSurfaceElevated,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = CptDragHandle)
        },
        content = content,
    )
}
