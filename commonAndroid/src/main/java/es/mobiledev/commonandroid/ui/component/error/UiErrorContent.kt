package es.mobiledev.commonandroid.ui.component.error

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.BlueGrey0
import es.mobiledev.commonandroid.theme.BlueGrey800
import es.mobiledev.commonandroid.theme.BlueGrey930
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.theme.Red0
import es.mobiledev.commonandroid.theme.titleLargeRobotoSemiBold
import es.mobiledev.commonandroid.theme.titleSmallRobotoRegular

/**
 * Icon, title, message and optional action shared by every error presentation.
 *
 * @param title headline of the error.
 * @param message body of the error.
 * @param modifier the [Modifier] to be applied to this component.
 * @param actionLabel label of the action button, [R.string.error_action_retry] when null.
 * @param onAction invoked when the action button is tapped, `null` to render no button.
 */
@Composable
fun UiErrorContent(
    @StringRes title: Int,
    @StringRes message: Int,
    modifier: Modifier = Modifier,
    @StringRes actionLabel: Int? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.error_content__outer_spacing)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.error_content__inner_spacing)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_info),
                tint = Red0,
                contentDescription = null,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.error_content__text_spacing)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(title),
                    style =
                        titleLargeRobotoSemiBold(
                            color = BlueGrey930,
                            textAlign = TextAlign.Center,
                        ),
                )
                Text(
                    text = stringResource(message),
                    style =
                        titleSmallRobotoRegular(
                            color = BlueGrey800,
                            textAlign = TextAlign.Center,
                        ),
                )
            }
        }
        onAction?.let { safeAction ->
            Button(
                onClick = safeAction,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BlueGrey800,
                        contentColor = BlueGrey0,
                    ),
            ) {
                Text(text = stringResource(actionLabel ?: R.string.error_action_retry))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewUiErrorContent() {
    CPTTheme {
        UiErrorContent(
            title = R.string.error_generic_title,
            message = R.string.error_generic_message,
            actionLabel = R.string.error_action_retry,
            onAction = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewUiErrorContentWithoutAction() {
    CPTTheme {
        UiErrorContent(
            title = R.string.error_generic_title,
            message = R.string.error_generic_message,
        )
    }
}
