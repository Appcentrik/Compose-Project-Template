package es.mobiledev.commonandroid.ui.base

import es.mobiledev.common.error.AppError
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.R
import kotlinx.coroutines.Dispatchers
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.core.error.ErrorAction
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.core.error.UiError
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BaseViewModelTest {
    private val loggedErrors = mutableListOf<Pair<String, AppError>>()
    private val logger =
        object : AppLogger {
            override fun logAppError(
                tag: String,
                error: AppError,
            ) {
                loggedErrors += tag to error
            }
        }

    private lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        loggedErrors.clear()
        viewModel = TestViewModel(logger)
    }

    @Test
    fun `starts idle`() {
        val status = viewModel.uiState.value.status

        assertFalse(status.isLoading)
        assertNull(status.blockingError)
        assertNull(status.snackbarError)
    }

    @Test
    fun `blocking error lands on the blocking channel and stops loading`() {
        viewModel.load()
        val error = screenError(retried = {})
        viewModel.show(error)

        val status = viewModel.uiState.value.status

        assertFalse(status.isLoading)
        assertSame(error, status.blockingError)
        assertNull(status.snackbarError)
    }

    @Test
    fun `snackbar error lands on the snackbar channel`() {
        viewModel.show(snackBarError)

        val status = viewModel.uiState.value.status

        assertEquals(snackBarError, status.snackbarError)
        assertNull(status.blockingError)
    }

    @Test
    fun `updateData never touches the error channels`() {
        val error = screenError(retried = {})
        viewModel.show(error)

        viewModel.update()

        assertSame(error, viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `updateData never clears a snackbar raised by another operation`() {
        viewModel.show(snackBarError)

        viewModel.update()

        assertEquals(snackBarError, viewModel.uiState.value.status.snackbarError)
    }

    @Test
    fun `showSuccess stops loading`() {
        viewModel.load()

        viewModel.success()

        assertFalse(viewModel.uiState.value.status.isLoading)
    }

    @Test
    fun `showSuccess never clears an error raised by a concurrent operation`() {
        val error = screenError(retried = {})
        viewModel.show(error)

        viewModel.success()
        viewModel.success()

        assertSame(error, viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `setLoading supersedes the errors on screen`() {
        viewModel.show(screenError(retried = {}))

        viewModel.load()

        val status = viewModel.uiState.value.status

        assertTrue(status.isLoading)
        assertNull(status.blockingError)
        assertNull(status.snackbarError)
    }

    @Test
    fun `setLoading drops the error so its action cannot outlive it`() {
        var retried = false
        viewModel.show(screenError(retried = { retried = true }))

        viewModel.load()
        viewModel.triggerAction()

        assertFalse("The action of a dismissed error must not run", retried)
    }

    @Test
    fun `onErrorAction runs the bound action and dismisses the error`() {
        var retried = false
        viewModel.show(screenError(retried = { retried = true }))

        viewModel.triggerAction()

        assertTrue(retried)
        assertNull(viewModel.uiState.value.status.blockingError)
        assertNull(viewModel.uiState.value.status.snackbarError)
    }

    @Test
    fun `onErrorAction is harmless when there is no error on screen`() {
        viewModel.triggerAction()

        assertEquals(emptyList<Pair<String, AppError>>(), loggedErrors)
    }

    @Test
    fun `dismissError clears both channels without running the action`() {
        var retried = false
        viewModel.show(screenError(retried = { retried = true }))

        viewModel.dismiss()

        assertFalse(retried)
        assertNull(viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `showSnackBarError logs the failure and binds the action`() {
        val error = AppError.NetworkError("no route to host")

        viewModel.showSnackBar(error, action = {})

        val status = viewModel.uiState.value.status

        assertTrue(status.snackbarError is UiError.SnackBar)
        assertEquals(listOf(TestViewModel::class.java.simpleName to error), loggedErrors)
    }

    @Test
    fun `the newest error replaces the previous one and takes over the action`() {
        var firstActionRan = false
        viewModel.show(screenError(retried = { firstActionRan = true }))

        viewModel.show(snackBarError)

        viewModel.triggerAction()

        assertFalse("The action of the replaced error must not run", firstActionRan)
        assertNull(viewModel.uiState.value.status.snackbarError)
    }

    @Test
    fun `an error without action still clears the previous one`() {
        var retried = false
        viewModel.show(screenError(retried = { retried = true }))

        viewModel.show(screenErrorWithoutAction)

        viewModel.triggerAction()

        assertFalse(retried)
        assertNull(viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `an error carries its action label together with the behaviour`() {
        viewModel.show(screenError(retried = {}))

        assertEquals(
            R.string.error_action_retry,
            viewModel.uiState.value.status.blockingError
                ?.action
                ?.label,
        )
    }

    @Test
    fun `an error without action has no label to render`() {
        viewModel.show(screenErrorWithoutAction)

        assertNull(
            viewModel.uiState.value.status.blockingError
                ?.action
        )
    }

    @Test
    fun `an error without action has no label on a snackbar either`() {
        viewModel.show(snackBarErrorWithoutAction)

        assertNull(
            viewModel.uiState.value.status.snackbarError
                ?.action
        )
    }

    @Test
    fun `each read of uiState exposes the same instance`() {
        val first = viewModel.uiState.value

        assertSame(first, viewModel.uiState.value)
    }

    private data class TestUiState(
        val value: Int = 0,
    )

    private class TestViewModel(
        appLogger: AppLogger,
        appDispatchers: AppDispatchers = testAppDispatchers,
    ) : BaseViewModel<TestUiState>(appLogger, appDispatchers) {
        override val mutableUiState: MutableStateFlow<UiState<TestUiState>> =
            MutableStateFlow(value = UiState(data = TestUiState()))

        fun update() = updateData { it }

        fun success() = showSuccess { it }

        fun load() = setLoading()

        fun show(uiError: UiError) = showError(uiError)

        fun showSnackBar(
            error: AppError,
            action: () -> Unit,
        ) = showSnackBarError(error, action)

        fun triggerAction() = onErrorAction()

        fun dismiss() = dismissError()
    }

    private companion object {
        /** An error carrying an action, so the test can tell whether pressing it ran the behaviour. */
        fun screenError(retried: () -> Unit) =
            UiError.Screen(
                title = UiErrorText.Resource(R.string.error_generic_title),
                message = UiErrorText.Resource(R.string.error_generic_message),
                action = ErrorAction(label = R.string.error_action_retry, onClick = retried),
            )

        val screenErrorWithoutAction =
            UiError.Screen(
                title = UiErrorText.Resource(R.string.error_generic_title),
                message = UiErrorText.Resource(R.string.error_generic_message),
            )

        val snackBarError =
            UiError.SnackBar(
                title = UiErrorText.Resource(R.string.error_generic_title),
                message = UiErrorText.Resource(R.string.error_generic_message),
                action = ErrorAction(label = R.string.error_action_retry, onClick = {}),
            )

        val snackBarErrorWithoutAction =
            UiError.SnackBar(
                title = UiErrorText.Resource(R.string.error_generic_title),
                message = UiErrorText.Resource(R.string.error_generic_message),
            )
    }
}

/** Dispatchers that run work eagerly, so a test asserts on the state without pumping any time. */
private val testAppDispatchers =
    AppDispatchers(
        main = Dispatchers.Unconfined,
        default = Dispatchers.Unconfined,
        io = Dispatchers.Unconfined,
    )
