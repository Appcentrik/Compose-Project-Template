package es.mobiledev.data.remote.error

import com.squareup.moshi.JsonDataException
import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResultException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class RemoteErrorHandlerTest {
    @Test
    fun `returns the value untouched when the call succeeds`() =
        runBlocking {
            val result = catchRemoteError { "ok" }

            assertEquals("ok", result)
        }

    @Test
    fun `maps HttpException to ServerError keeping the status code`() {
        val error = captureError { throw httpException(503) }

        assertTrue("Expected ServerError but was $error", error is AppError.ServerError)
        assertEquals(503, (error as AppError.ServerError).code)
    }

    @Test
    fun `maps IOException to NetworkError`() {
        val error = captureError { throw IOException("no route to host") }

        assertTrue("Expected NetworkError but was $error", error is AppError.NetworkError)
    }

    @Test
    fun `maps SocketTimeoutException to TimeoutError and not to NetworkError`() {
        val error = captureError { throw SocketTimeoutException("read timed out") }

        assertTrue("Expected TimeoutError but was $error", error is AppError.TimeoutError)
    }

    @Test
    fun `maps 401 and 403 to UnauthorizedError`() {
        listOf(401, 403).forEach { code ->
            val error = captureError { throw httpException(code) }

            assertTrue("Expected UnauthorizedError for $code but was $error", error is AppError.UnauthorizedError)
        }
    }

    @Test
    fun `maps 404 to NotFoundError`() {
        val error = captureError { throw httpException(404) }

        assertTrue("Expected NotFoundError but was $error", error is AppError.NotFoundError)
    }

    @Test
    fun `maps JsonDataException to ParseError`() {
        val error = captureError { throw JsonDataException("malformed payload") }

        assertTrue("Expected ParseError but was $error", error is AppError.ParseError)
    }

    @Test
    fun `maps any other Exception to UnknownError`() {
        val error = captureError { throw IllegalStateException("boom") }

        assertTrue("Expected UnknownError but was $error", error is AppError.UnknownError)
    }

    @Test
    fun `keeps the original AppError when the call throws AsyncResultException`() {
        val original = AppError.NetworkError("already mapped")

        val error = captureError { throw AsyncResultException(original) }

        assertSame(
            "AsyncResultException must not be re-wrapped, the original AppError was lost",
            original,
            error,
        )
    }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException instead of wrapping it`() {
        runBlocking { catchRemoteError { throw CancellationException("cancelled") } }
    }

    @Test
    fun `falls back to the exception class name when there is no message`() {
        val error = captureError { throw IOException() }

        assertEquals("IOException", error.message)
    }

    private fun captureError(block: suspend () -> Nothing): AppError =
        runBlocking {
            try {
                catchRemoteError(block)
                throw AssertionError("Expected the call to throw")
            } catch (e: AsyncResultException) {
                e.error
            }
        }

    private fun httpException(code: Int): HttpException =
        HttpException(
            Response.error<Any>(code, "".toResponseBody()),
        )
}
