package es.mobiledev.data.remote.error

import es.mobiledev.common.error.AppError
import es.mobiledev.common.response.AsyncResult
import es.mobiledev.common.response.AsyncResultException
import es.mobiledev.common.util.AppDispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class RemoteResponseTest {
    private val dispatchers =
        AppDispatchers(
            main = Dispatchers.Unconfined,
            default = Dispatchers.Unconfined,
            io = Dispatchers.Unconfined,
        )

    @Test
    fun `wraps the returned value in a Success`() =
        runTest {
            val result = remoteResponse(dispatchers) { "ok" }.flow().first()

            assertEquals(AsyncResult.Success("ok"), result)
        }

    @Test
    fun `maps an unmapped failure through the remote mapping`() =
        runTest {
            val result = remoteResponse(dispatchers) { throw IOException("no route to host") }.flow().first()

            assertTrue((result as AsyncResult.Error).error is AppError.NetworkError)
        }

    @Test
    fun `keeps the original AppError when the call already mapped it`() =
        runTest {
            val original = AppError.UnauthorizedError(message = "no session")
            val result = remoteResponse(dispatchers) { throw AsyncResultException(original) }.flow().first()

            assertEquals(original, (result as AsyncResult.Error).error)
        }

    @Test(expected = CancellationException::class)
    fun `rethrows CancellationException instead of reporting it as an error`() {
        runTest { remoteResponse(dispatchers) { throw CancellationException("cancelled") }.flow().first() }
    }

    @Test
    fun `runs the call on every collection`() =
        runTest {
            var calls = 0
            val flow = remoteResponse(dispatchers) { ++calls }.flow()

            assertEquals(AsyncResult.Success(1), flow.first())
            assertEquals(AsyncResult.Success(2), flow.first())

            assertEquals("Each collection must run the call again", 2, calls)
        }
}
