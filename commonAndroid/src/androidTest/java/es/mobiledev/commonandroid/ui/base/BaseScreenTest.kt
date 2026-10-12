package es.mobiledev.commonandroid.ui.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.core.logging.LogcatAppLogger
import es.mobiledev.commonandroid.ui.component.error.UiError
import es.mobiledev.commonandroid.ui.component.error.UiErrorPresentation
import es.mobiledev.commonandroid.ui.component.error.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Drives [BaseScreen] with a real [BaseViewModel], so the whole chain runs without needing Hilt:
 * AppError mapping, the two error channels, the snackbar lifecycle and the action routing.
 */
@RunWith(AndroidJUnit4::class)
class BaseScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun aBlockingErrorIsRenderedOverTheContent() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.show(AppError.ServerError(code = 500, message = "boom"), UiErrorPresentation.Screen)
        composeRule.waitForIdle()

        composeRule.onNodeWithText(SERVER_TITLE).assertIsDisplayed()
    }

    @Test
    fun theContentStaysComposedUnderABlockingError() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.show(AppError.ServerError(code = 500, message = "boom"), UiErrorPresentation.Screen)
        composeRule.waitForIdle()

        assertTrue(
            "The content must stay composed so the scroll position survives a blocking error",
            composeRule.onAllNodesWithText(MARKER).fetchSemanticsNodes().isNotEmpty(),
        )
    }

    @Test
    fun aNewBlockingErrorSupersedesAPreviousSnackBar() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.showSnackBar(AppError.NetworkError("network"))
        viewModel.show(AppError.ServerError(code = 500, message = "boom"), UiErrorPresentation.Screen)

        val status = viewModel.uiState.value.status
        assertNull(status.snackbarError)
        assertTrue(status.blockingError is UiError.Screen)
    }

    @Test
    fun aSuccessDoesNotClearTheErrorOfAnotherOperation() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.showSnackBar(AppError.NetworkError("network"))
        viewModel.success()
        composeRule.waitForIdle()

        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()
    }

    @Test
    fun theSnackBarActionReachesTheViewModel() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.showSnackBar(AppError.NetworkError("network"))
        composeRule.waitForIdle()

        composeRule.onNodeWithText(RETRY).performClick()
        composeRule.waitForIdle()

        assertEquals("The error action must run exactly once", 1, viewModel.actionsInvoked)
        assertNull("The error must be dismissed after acting", viewModel.uiState.value.status.snackbarError)
    }

    @Test
    fun aSecondSnackBarIsShownAfterTheFirstOneIsDismissed() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.showSnackBar(AppError.NetworkError("network"))
        composeRule.onNodeWithText(NETWORK_TITLE).assertIsDisplayed()

        composeRule.onNodeWithText(RETRY).performClick()
        composeRule.waitForIdle()

        viewModel.showSnackBar(AppError.ServerError(code = 500, message = "boom"))
        composeRule.waitForIdle()

        composeRule.onNodeWithText(SERVER_TITLE).assertIsDisplayed()
    }

    @Test
    fun loadingKeepsTheContentComposed() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.load()
        composeRule.waitForIdle()

        assertTrue(viewModel.uiState.value.status.isLoading)
        assertTrue(
            "Loading must not unmount the content",
            composeRule.onAllNodesWithText(MARKER).fetchSemanticsNodes().isNotEmpty(),
        )
    }

    @Test
    fun loadingReplacesAPreviousBlockingError() {
        val viewModel = TestScreenViewModel()
        setScreen(viewModel)

        viewModel.show(AppError.ServerError(code = 500, message = "boom"), UiErrorPresentation.Screen)
        viewModel.load()
        composeRule.waitForIdle()

        val status = viewModel.uiState.value.status
        assertTrue(status.isLoading)
        assertNull("A fresh load supersedes the errors on screen", status.blockingError)
    }

    private fun setScreen(viewModel: TestScreenViewModel) {
        composeRule.setContent {
            val state by viewModel.uiState.collectAsState()
            BaseScreen(
                status = state.status,
                onRetry = viewModel::onErrorAction,
                onErrorDismiss = viewModel::dismissError,
            ) { paddingValues ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                ) {
                    Text(text = MARKER)
                }
            }
        }
        composeRule.waitForIdle()
    }

    private data class TestUiState(
        val marker: String = MARKER,
    )

    private class TestScreenViewModel(
        private val appLogger: AppLogger = LogcatAppLogger,
    ) : BaseViewModel<TestUiState>(appLogger) {
        override val mutableUiState: MutableStateFlow<UiState<TestUiState>> =
            MutableStateFlow(value = UiState(data = TestUiState()))

        var actionsInvoked = 0
            private set

        fun show(
            error: AppError,
            presentation: UiErrorPresentation,
        ) = showError(
            uiError = error.toUiError(presentation),
            action = { actionsInvoked++ },
        )

        fun showSnackBar(error: AppError) = showSnackBarError(error = error, action = { actionsInvoked++ })

        fun success() = showSuccess { it }

        fun load() = setLoading()
    }

    private companion object {
        const val MARKER = "content marker"
        const val NETWORK_TITLE = "No internet connection"
        const val SERVER_TITLE = "Server error"
        const val RETRY = "Retry"
    }
}
