package es.mobiledev.commonandroid.ui.component.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.core.error.resolve
import es.mobiledev.commonandroid.theme.BlueGrey800
import es.mobiledev.commonandroid.theme.BlueGrey930
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.theme.CptBtnContentColor
import es.mobiledev.commonandroid.theme.Red0
import es.mobiledev.commonandroid.theme.titleLargeRobotoSemiBold
import es.mobiledev.commonandroid.theme.titleSmallRobotoRegular

@Composable
fun UiErrorContent(
    uiError: UiError,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.error_content__outer_spacing),
            ),
        modifier =
            modifier.widthIn(
                max = dimensionResource(id = R.dimen.error_content__max_width),
            ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.error_content__inner_spacing),
                ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = null,
                tint = Red0,
                modifier = Modifier.size(dimensionResource(id = R.dimen.error_content__icon_size))
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(
                        space = dimensionResource(id = R.dimen.error_content__text_spacing),
                    ),
            ) {
                Text(
                    text = uiError.title.resolve(),
                    style =
                        titleLargeRobotoSemiBold(
                            color = BlueGrey930,
                            textAlign = TextAlign.Center,
                        ),
                )
                Text(
                    text = uiError.message.resolve(),
                    style =
                        titleSmallRobotoRegular(
                            color = BlueGrey800,
                            textAlign = TextAlign.Center,
                        ),
                )
            }
        }
        uiError.action?.let { errorAction ->
            Button(
                onClick = {
                    onDismiss()
                    errorAction.onClick()
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BlueGrey800,
                        contentColor = CptBtnContentColor,
                    ),
            ) {
                Text(
                    text = stringResource(id = errorAction.label),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    CPTTheme {
        UiErrorContent(
            uiError =
                UiError.SnackBar(
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
