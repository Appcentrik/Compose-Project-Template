package es.mobiledev.cpt.ui.screen.testNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.theme.CPTTheme
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation

@Composable
fun TestScreen() {
    val viewModel: TestViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        status = uiState.status,
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
        ) {
            Text(
                text = uiState.data.lastAction,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            TestHeader(title = stringResource(R.string.error_test_section_presentation))
            UiErrorPresentation.entries.forEach { presentation ->
                TestButton(
                    label = stringResource(labelOf(presentation)),
                    onClick = { viewModel.onShowError(AppError.NetworkError("network"), presentation) },
                )
            }

            TestHeader(title = stringResource(R.string.error_test_section_type))
            mockErrors.forEach { error ->
                TestButton(
                    label = stringResource(labelOf(error)),
                    onClick = { viewModel.onShowTransientError(error, withAction = true) },
                )
            }

            TestHeader(title = stringResource(R.string.error_test_section_lifecycle))
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_loading),
                onClick = viewModel::onStartLoading,
            )
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_success),
                onClick = viewModel::onSuccessKeepingTheError,
            )
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_snackbar_auto),
                onClick = { viewModel.onShowTransientError(AppError.ParseError("parse"), withAction = false) },
            )
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_snackbar_sequence),
                onClick = viewModel::onShowTransientErrorsInSequence,
            )
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_dismiss),
                onClick = viewModel::onDismiss,
            )
            TestButton(
                label = stringResource(R.string.error_test_lifecycle_dismiss_action),
                onClick = viewModel::onDismissWithAction,
            )

            HorizontalDivider()

            TestHeader(title = stringResource(R.string.error_test_section_counters))
            Text(
                text = stringResource(R.string.error_test_counter_actions, uiState.data.actionInvocations),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.error_test_counter_snackbars, uiState.data.snackBarsShown),
                style = MaterialTheme.typography.bodyLarge,
            )
            TestButton(
                label = stringResource(R.string.error_test_counter_reset),
                onClick = viewModel::onResetCounters,
            )

            HorizontalDivider()

            TestButton(
                label = stringResource(R.string.error_test_navigation_smoke),
                onClick = viewModel::onNavigationSmokeTest,
            )
        }
    }
}

@Composable
private fun TestHeader(
    title: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun TestButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = label)
    }
}

private fun labelOf(
    presentation: UiErrorPresentation,
): Int =
    when (presentation) {
        UiErrorPresentation.Screen -> R.string.error_test_show_screen
        UiErrorPresentation.Dialog -> R.string.error_test_show_dialog
        UiErrorPresentation.Sheet -> R.string.error_test_show_sheet
        UiErrorPresentation.SnackBar -> R.string.error_test_show_snackbar
        UiErrorPresentation.Embedded -> R.string.error_test_show_embedded
    }

private fun labelOf(
    error: AppError,
): Int =
    when (error) {
        is AppError.NetworkError -> R.string.error_network_title
        is AppError.ServerError -> R.string.error_server_title
        is AppError.TimeoutError -> R.string.error_timeout_title
        is AppError.UnauthorizedError -> R.string.error_unauthorized_title
        is AppError.NotFoundError -> R.string.error_not_found_title
        is AppError.ParseError -> R.string.error_parse_title
        is AppError.LocalError -> R.string.error_local_title
        is AppError.UnknownError -> R.string.error_unknown_title
    }

private val mockErrors =
    listOf(
        AppError.NetworkError("network"),
        AppError.ServerError(code = 500, message = "server"),
        AppError.TimeoutError("timeout"),
        AppError.UnauthorizedError("unauthorized"),
        AppError.NotFoundError("not found"),
        AppError.ParseError("parse"),
        AppError.LocalError("local"),
        AppError.UnknownError("unknown"),
    )

@PreviewLightDark
@Composable
private fun TestScreenPreview() {
    CPTTheme {
        BaseScreen { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
            ) {
                TestHeader(title = stringResource(R.string.error_test_section_lifecycle))
                TestButton(
                    label = stringResource(R.string.error_test_lifecycle_loading),
                    onClick = {},
                )
                TestButton(
                    label = stringResource(R.string.error_test_lifecycle_snackbar_sequence),
                    onClick = {},
                )
            }
        }
    }
}
