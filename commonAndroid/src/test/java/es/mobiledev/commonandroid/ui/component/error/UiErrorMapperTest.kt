package es.mobiledev.commonandroid.ui.component.error

import es.mobiledev.common.error.AppError
import es.mobiledev.commonandroid.core.error.UiErrorText
import es.mobiledev.commonandroid.R
import es.mobiledev.commonandroid.core.error.UiError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UiErrorMapperTest {
    private val allAppErrors =
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

    @Test
    fun `maps every app error to every presentation`() {
        allAppErrors.forEach { appError ->
            UiErrorPresentation.entries.forEach { presentation ->
                val uiError = appError.toUiError(presentation, action = {})

                assertNotEquals(R.string.empty_string, uiError.title)
                assertNotEquals(R.string.empty_string, uiError.message)
                assertEquals(R.string.error_action_retry, uiError.action?.label)
            }
        }
    }

    @Test
    fun `builds the variant matching the requested presentation`() {
        val appError = AppError.UnknownError("unknown")

        val presentations =
            mapOf(
                UiErrorPresentation.Screen to UiError.Screen::class,
                UiErrorPresentation.Dialog to UiError.Dialog::class,
                UiErrorPresentation.Sheet to UiError.Sheet::class,
                UiErrorPresentation.SnackBar to UiError.SnackBar::class,
                UiErrorPresentation.Embedded to UiError.Embedded::class,
            )

        presentations.forEach { (presentation, expected) ->
            assertEquals(
                "Wrong variant for $presentation",
                expected,
                appError.toUiError(presentation)::class,
            )
        }
    }

    @Test
    fun `only the snackbar presentation is not blocking`() {
        allAppErrors.forEach { appError ->
            UiErrorPresentation.entries.forEach { presentation ->
                assertEquals(
                    "Wrong isBlocking for $presentation",
                    presentation != UiErrorPresentation.SnackBar,
                    appError.toUiError(presentation).isBlocking,
                )
            }
        }
    }

    @Test
    fun `maps network error to the network message`() {
        val uiError = AppError.NetworkError("network").toUiError(UiErrorPresentation.Screen)

        assertEquals(UiErrorText.Resource(R.string.error_network_title), uiError.title)
        assertEquals(UiErrorText.Resource(R.string.error_network_message), uiError.message)
    }

    @Test
    fun `maps local error to the local message`() {
        val uiError = AppError.LocalError("local").toUiError(UiErrorPresentation.SnackBar)

        assertEquals(UiErrorText.Resource(R.string.error_local_title), uiError.title)
        assertEquals(UiErrorText.Resource(R.string.error_local_message), uiError.message)
    }

    @Test
    fun `gives every AppError its own copy`() {
        val expected =
            mapOf(
                AppError.NetworkError("network") to (R.string.error_network_title to R.string.error_network_message),
                AppError.TimeoutError("timeout") to (R.string.error_timeout_title to R.string.error_timeout_message),
                AppError.UnauthorizedError("unauthorized") to
                    (R.string.error_unauthorized_title to R.string.error_unauthorized_message),
                AppError.NotFoundError("not found") to
                    (R.string.error_not_found_title to R.string.error_not_found_message),
                AppError.ParseError("parse") to (R.string.error_parse_title to R.string.error_parse_message),
                AppError.LocalError("local") to (R.string.error_local_title to R.string.error_local_message),
                AppError.UnknownError("unknown") to (R.string.error_unknown_title to R.string.error_unknown_message),
            )

        expected.forEach { (appError, copy) ->
            val uiError = appError.toUiError(UiErrorPresentation.Screen)

            assertEquals(
                "Wrong title for $appError",
                UiErrorText.Resource(copy.first),
                uiError.title,
            )
            assertEquals(
                "Wrong message for $appError",
                UiErrorText.Resource(copy.second),
                uiError.message,
            )
        }
    }

    @Test
    fun `defaults the action label to retry and renders no action when none is given`() {
        val appError = AppError.UnknownError("unknown")

        assertEquals(
            R.string.error_action_retry,
            appError.toUiError(UiErrorPresentation.Screen, action = {}).action?.label,
        )
        assertNull(appError.toUiError(UiErrorPresentation.Screen).action)
    }

    @Test
    fun `keeps the action given to the mapper bound to the produced error`() {
        var retried = false

        val uiError = AppError.UnknownError("unknown").toUiError(UiErrorPresentation.Screen, action = { retried = true })

        uiError.action?.onClick?.invoke()

        assertTrue(retried)
    }

    @Test
    fun `shows the generic copy when no override is given`() {
        val uiError = AppError.ServerError(code = 500, message = "HTTP 500 Internal Server Error").toUiError(UiErrorPresentation.Screen)

        assertEquals(UiErrorText.Resource(R.string.error_server_title), uiError.title)
        assertEquals(UiErrorText.Resource(R.string.error_server_message), uiError.message)
    }

    @Test
    fun `a title given to the mapper replaces the generic one`() {
        val uiError =
            AppError.UnknownError("unknown").toUiError(
                presentation = UiErrorPresentation.Screen,
                title = UiErrorText.Raw("Tu sesión ha caducado"),
            )

        assertEquals(UiErrorText.Raw("Tu sesión ha caducado"), uiError.title)
        assertEquals(UiErrorText.Resource(R.string.error_unknown_message), uiError.message)
    }

    @Test
    fun `a message given to the mapper replaces the generic one`() {
        val uiError =
            AppError.UnknownError("unknown").toUiError(
                presentation = UiErrorPresentation.SnackBar,
                message = UiErrorText.Raw("Vuelve a intentarlo en unos minutos"),
            )

        assertEquals(UiErrorText.Resource(R.string.error_unknown_title), uiError.title)
        assertEquals(UiErrorText.Raw("Vuelve a intentarlo en unos minutos"), uiError.message)
    }

    @Test
    fun `both texts can come from the backend at once`() {
        val uiError =
            AppError.ServerError(code = 503, message = "down").toUiError(
                presentation = UiErrorPresentation.Screen,
                title = UiErrorText.Raw("Servicio no disponible"),
                message = UiErrorText.Raw("Estamos trabajan en ello"),
            )

        assertEquals(UiErrorText.Raw("Servicio no disponible"), uiError.title)
        assertEquals(UiErrorText.Raw("Estamos trabajan en ello"), uiError.message)
    }

    @Test
    fun `ignores the technical message since every failure has a generic copy`() {
        val uiError =
            AppError.ServerError(code = 503, message = "HTTP 503 Service Unavailable").toUiError(
                presentation = UiErrorPresentation.Screen,
            )

        assertEquals(UiErrorText.Resource(R.string.error_server_title), uiError.title)
        assertEquals(UiErrorText.Resource(R.string.error_server_message), uiError.message)
    }

    @Test
    fun `keeps a meaningful equals so it can live inside UiState`() {
        val appError = AppError.UnknownError("unknown")

        assertEquals(
            appError.toUiError(UiErrorPresentation.SnackBar),
            appError.toUiError(UiErrorPresentation.SnackBar),
        )
        assertTrue(
            appError.toUiError(UiErrorPresentation.SnackBar) !=
                appError.toUiError(UiErrorPresentation.Screen),
        )
    }
}
