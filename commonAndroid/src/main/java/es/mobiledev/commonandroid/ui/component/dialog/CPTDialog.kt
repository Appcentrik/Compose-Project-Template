package es.mobiledev.commonandroid.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey800
import es.mobiledev.commonandroid.theme.CptSurfaceElevated
import es.mobiledev.commonandroid.theme.getDialogShape

/**
 * Common dialog shell.
 *
 * Handles the window, centering, surface, padding and optional close button so feature dialogs only
 * provide their content.
 *
 * @param onDismissRequest invoked when the user dismisses the dialog.
 * @param modifier the [Modifier] to be applied to the dialog window.
 * @param showCloseButton whether the close icon button is displayed.
 * @param content the content of the dialog. It is already padded by the card.
 */
@Composable
fun CPTDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showCloseButton: Boolean = false,
    canDismissOnBackPress: Boolean = true,
    canDismissOnClickOutside: Boolean = true,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties =
            DialogProperties(
                dismissOnBackPress = canDismissOnBackPress,
                dismissOnClickOutside = canDismissOnClickOutside,
                usePlatformDefaultWidth = false,
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.cpt_dialog__margin)),
        ) {
            Card(
                colors =
                    CardDefaults.cardColors(
                        containerColor = CptSurfaceElevated,
                    ),
                shape = getDialogShape(),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .widthIn(max = dimensionResource(id = R.dimen.cpt_dialog__max_width)),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.spacedBy(
                            space = dimensionResource(id = R.dimen.cpt_dialog__gap),
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.cpt_dialog__padding)),
                ) {
                    if (showCloseButton) {
                        IconButton(
                            onClick = onDismissRequest,
                            modifier =
                                Modifier
                                    .align(alignment = Alignment.End)
                                    .size(
                                        size =
                                            dimensionResource(id = R.dimen.cpt_dialog__close_size),
                                    ),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription =
                                    stringResource(id = R.string.dialog_action_close),
                                tint = BlueGrey800,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    content()
                }
            }
        }
    }
}
