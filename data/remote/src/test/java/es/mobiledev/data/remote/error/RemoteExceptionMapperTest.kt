package es.mobiledev.data.remote.error

import com.squareup.moshi.JsonDataException
import es.mobiledev.common.error.AppError
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.InterruptedIOException
import java.io.IOException
import java.net.SocketTimeoutException

class RemoteExceptionMapperTest {
    @Test
    fun `maps HttpException to ServerError keeping the status code`() {
        val error = manageException(httpException(503))

        assertTrue("Expected ServerError but was $error", error is AppError.ServerError)
        assertEquals(503, (error as AppError.ServerError).code)
    }

    @Test
    fun `maps IOException to NetworkError`() {
        val error = manageException(IOException("no route to host"))

        assertTrue("Expected NetworkError but was $error", error is AppError.NetworkError)
    }

    @Test
    fun `maps SocketTimeoutException to TimeoutError and not to NetworkError`() {
        val error = manageException(SocketTimeoutException("read timed out"))

        assertTrue("Expected TimeoutError but was $error", error is AppError.TimeoutError)
    }

    @Test
    fun `maps a call timeout to TimeoutError and not to NetworkError`() {
        val error = manageException(InterruptedIOException("timeout"))

        assertTrue("Expected TimeoutError but was $error", error is AppError.TimeoutError)
    }

    @Test
    fun `maps 401 and 403 to UnauthorizedError`() {
        listOf(401, 403).forEach { code ->
            val error = manageException(httpException(code))

            assertTrue("Expected UnauthorizedError for $code but was $error", error is AppError.UnauthorizedError)
        }
    }

    @Test
    fun `maps 404 to NotFoundError`() {
        val error = manageException(httpException(404))

        assertTrue("Expected NotFoundError but was $error", error is AppError.NotFoundError)
    }

    @Test
    fun `maps JsonDataException to ParseError`() {
        val error = manageException(JsonDataException("malformed payload"))

        assertTrue("Expected ParseError but was $error", error is AppError.ParseError)
    }

    @Test
    fun `maps any other Exception to UnknownError`() {
        val error = manageException(IllegalStateException("boom"))

        assertTrue("Expected UnknownError but was $error", error is AppError.UnknownError)
    }

    @Test
    fun `falls back to the exception class name when there is no message`() {
        assertEquals("IOException", manageException(IOException()).message)
    }

    private fun httpException(code: Int): HttpException =
        HttpException(
            Response.error<Any>(code, "".toResponseBody()),
        )
}
