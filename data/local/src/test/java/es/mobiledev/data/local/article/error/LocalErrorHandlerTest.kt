package es.mobiledev.data.local.article.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResultException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class LocalErrorHandlerTest {
    @Test
    fun `returns the value untouched when the call succeeds`() =
        runBlocking {
            val result = catchLocalError { 42 }

            assertEquals(42, result)
        }

    @Test
    fun `maps any Exception to LocalError keeping the message`() {
        val error = captureError { throw IOException("database is locked") }

        assertTrue("Expected LocalError but was $error", error is AppError.LocalError)
        assertEquals("database is locked", error.message)
    }

    @Test
    fun `keeps the original AppError when the call throws AsyncResultException`() {
        val original = AppError.ServerError(code = 500, message = "already mapped")

        val error = captureError { throw AsyncResultException(original) }

        assertSame(
            "AsyncResultException must not be re-wrapped, the original AppError was lost",
            original,
            error,
        )
    }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException instead of wrapping it`() {
        runBlocking { catchLocalError { throw CancellationException("cancelled") } }
    }

    @Test
    fun `falls back to the class name when the exception has no message`() {
        val error = captureError { throw IOException() }

        assertEquals("IOException", error.message)
    }

    private fun captureError(block: suspend () -> Nothing): AppError =
        runBlocking {
            try {
                catchLocalError(block)
                throw AssertionError("Expected the call to throw")
            } catch (e: AsyncResultException) {
                e.error
            }
        }
}
