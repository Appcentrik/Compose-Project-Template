package es.mobiledev.commonandroid.ui.base

import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.ui.component.error.UiError
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
        viewModel.show(screenError, action = {})

        val status = viewModel.uiState.value.status

        assertFalse(status.isLoading)
        assertEquals(screenError, status.blockingError)
        assertNull(status.snackbarError)
    }

    @Test
    fun `snackbar error lands on the snackbar channel`() {
        viewModel.show(snackBarError, action = {})

        val status = viewModel.uiState.value.status

        assertEquals(snackBarError, status.snackbarError)
        assertNull(status.blockingError)
    }

    @Test
    fun `updateData never touches the error channels`() {
        viewModel.show(screenError, action = {})

        viewModel.update()

        assertEquals(screenError, viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `updateData never clears a snackbar raised by another operation`() {
        viewModel.show(snackBarError, action = {})

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
        viewModel.show(screenError, action = {})

        viewModel.success()
        viewModel.success()

        assertEquals(screenError, viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `setLoading supersedes the errors on screen`() {
        viewModel.show(screenError, action = {})

        viewModel.load()

        val status = viewModel.uiState.value.status

        assertTrue(status.isLoading)
        assertNull(status.blockingError)
        assertNull(status.snackbarError)
    }

    @Test
    fun `setLoading drops the pending action so it cannot outlive its error`() {
        var retried = false
        viewModel.show(screenError) { retried = true }

        viewModel.load()
        viewModel.triggerAction()

        assertFalse("The action of a dismissed error must not run", retried)
    }

    @Test
    fun `onErrorAction runs the bound action and dismisses the error`() {
        var retried = false
        viewModel.show(screenError) { retried = true }

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
        viewModel.show(screenError) { retried = true }

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
        viewModel.show(screenError) { firstActionRan = true }

        viewModel.show(snackBarError, action = {})

        viewModel.triggerAction()

        assertFalse("The action of the replaced error must not run", firstActionRan)
        assertNull(viewModel.uiState.value.status.snackbarError)
    }

    @Test
    fun `an error without action still clears the previous one`() {
        var retried = false
        viewModel.show(screenError) { retried = true }

        viewModel.show(screenErrorWithoutAction)

        viewModel.triggerAction()

        assertFalse(retried)
        assertNull(viewModel.uiState.value.status.blockingError)
    }

    @Test
    fun `an error without action drops its action label`() {
        viewModel.show(screenError, action = null)

        assertNull(
            viewModel.uiState.value.status.blockingError
                ?.actionLabel
        )
    }

    @Test
    fun `an error without action drops the action label of a snackbar`() {
        viewModel.show(snackBarError, action = null)

        assertNull(
            viewModel.uiState.value.status.snackbarError
                ?.actionLabel
        )
    }

    @Test
    fun `an error with action keeps its action label`() {
        viewModel.show(screenError, action = {})

        assertEquals(
            R.string.error_action_retry,
            viewModel.uiState.value.status.blockingError
                ?.actionLabel
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
    ) : BaseViewModel<TestUiState>(appLogger) {
        override val mutableUiState: MutableStateFlow<UiState<TestUiState>> =
            MutableStateFlow(value = UiState(data = TestUiState()))

        fun update() = updateData { it }

        fun success() = showSuccess { it }

        fun load() = setLoading()

        fun show(
            uiError: UiError,
            action: (() -> Unit)? = null,
        ) = showError(uiError, action)

        fun showSnackBar(
            error: AppError,
            action: () -> Unit,
        ) = showSnackBarError(error, action)

        fun triggerAction() = onErrorAction()

        fun dismiss() = dismissError()
    }

    private companion object {
        val screenError =
            UiError.Screen(
                title = R.string.error_generic_title,
                message = R.string.error_generic_message,
                actionLabel = R.string.error_action_retry,
            )

        val screenErrorWithoutAction =
            UiError.Screen(
                title = R.string.error_generic_title,
                message = R.string.error_generic_message,
            )

        val snackBarError =
            UiError.SnackBar(
                title = R.string.error_generic_title,
                message = R.string.error_generic_message,
                actionLabel = R.string.error_action_retry,
            )
    }
}
