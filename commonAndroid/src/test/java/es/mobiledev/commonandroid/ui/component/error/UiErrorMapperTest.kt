package es.mobiledev.commonandroid.ui.component.error

import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiErrorMapperTest {
    @Test
    fun `maps every app error to a snackbar`() {
        val errors =
            listOf(
                AppError.NetworkError("network"),
                AppError.ServerError(code = 500, message = "server"),
                AppError.ParseError("parse"),
                AppError.LocalError("local"),
                AppError.UnknownError("unknown"),
            )

        errors.forEach { error ->
            val uiError = error.toUiError<UiError.SnackBar>()

            assertTrue(uiError is UiError.SnackBar)
            assertTrue(uiError.title != 0)
            assertTrue(uiError.message != 0)
        }
    }

    @Test
    fun `maps network error to the network message`() {
        val uiError = AppError.NetworkError("network").toUiError<UiError.Screen>()

        assertEquals(R.string.error_network_title, uiError.title)
        assertEquals(R.string.error_network_message, uiError.message)
    }

    @Test
    fun `preserves the retry action`() {
        var retried = false
        val uiError =
            AppError.LocalError("local").toUiError<UiError.Dialog> {
                retried = true
            }

        uiError.action?.invoke()

        assertTrue(retried)
    }
}
