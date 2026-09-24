package es.mobiledev.cpt.ui.screen.testNavigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.ui.base.BaseScreen
import es.mobiledev.commonandroid.ui.component.error.UiError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen() {
    val viewModel: TestViewModel = hiltViewModel()
    val uiState by viewModel.getUiState().collectAsStateWithLifecycle()
    var uiError by remember { mutableStateOf<UiError>(UiError.None) }

    fun showError(error: UiError) {
        uiError = error
    }

    BaseScreen(
        isLoading = uiState.isLoading,
        uiError = uiError,
        onUiErrorDismiss = { uiError = UiError.None },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.test),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(uiState.data.title)
            Button(
                onClick = {
                    showError(
                        UiError.SnackBar(
                            title = R.string.error_test_snackbar_title,
                            message = R.string.error_test_snackbar_message,
                            action = { uiError = UiError.None },
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.error_test_show_snackbar))
            }
            Button(
                onClick = {
                    showError(
                        UiError.Dialog(
                            title = R.string.error_test_dialog_title,
                            message = R.string.error_test_dialog_message,
                            action = { uiError = UiError.None },
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.error_test_show_dialog))
            }
            Button(
                onClick = {
                    showError(
                        UiError.Sheet(
                            title = R.string.error_test_sheet_title,
                            message = R.string.error_test_sheet_message,
                            action = { uiError = UiError.None },
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.error_test_show_sheet))
            }
            Button(
                onClick = {
                    showError(
                        UiError.Screen(
                            title = R.string.error_test_screen_title,
                            message = R.string.error_test_screen_message,
                            action = { uiError = UiError.None },
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.error_test_show_screen))
            }
            Button(
                onClick = {
                    showError(
                        UiError.Embedded(
                            title = R.string.error_test_embedded_title,
                            message = R.string.error_test_embedded_message,
                            action = { uiError = UiError.None },
                        ),
                    )
                },
            ) {
                Text(stringResource(R.string.error_test_show_embedded))
            }
        }
    }
}
