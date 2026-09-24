package es.mobiledev.commonandroid.ui.component.error

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

@Composable
fun UiErrorContent(
    uiError: UiError,
    modifier: Modifier = Modifier,
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
                    text = stringResource(uiError.title),
                    style =
                        titleLargeRobotoSemiBold(
                            color = BlueGrey930,
                            textAlign = TextAlign.Center,
                        ),
                )
                Text(
                    text = stringResource(uiError.message),
                    style =
                        titleSmallRobotoRegular(
                            color = BlueGrey800,
                            textAlign = TextAlign.Center,
                        ),
                )
            }
        }
        uiError.action?.let { safeErrorAction ->
            Button(
                onClick = safeErrorAction,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BlueGrey800,
                        contentColor = BlueGrey0,
                    ),
            ) {
                Text(text = stringResource(R.string.error_action_confirm))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewUiErrorContent() {
    CPTTheme {
        UiErrorContent(
            uiError =
                UiError.Screen(
                    title = R.string.error_generic_title,
                    message = R.string.error_generic_message,
                    action = {},
                ),
        )
    }
}
