package es.mobiledev.common.response

import es.mobiledev.common.error.AppError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponseExtensionsTest {
    @Test
    fun `remoteResult returns Success when the block returns normally`() =
        runBlocking {
            val result = remoteResult { "ok" }

            assertEquals(AsyncResult.Success("ok"), result)
        }

    @Test
    fun `remoteResult returns Error preserving the already mapped AppError`() =
        runBlocking {
            val serverError = AppError.ServerError(code = 500, message = "boom")

            val result = remoteResult<String> { throw AsyncResultException(serverError) }

            assertEquals(AsyncResult.Error(serverError), result)
        }

    @Test
    fun `remoteResult maps an unmapped exception to UnknownError`() =
        runBlocking {
            val result = remoteResult<String> { throw IllegalStateException("boom") }

            val error = result.unwrapError()
            assertTrue("Expected UnknownError but was $error", error is AppError.UnknownError)
            assertEquals("boom", error.message)
        }

    @Test
    fun `localResult maps an unmapped exception to LocalError`() =
        runBlocking {
            val result = localResult<String> { throw IllegalStateException("disk full") }

            assertTrue("Expected LocalError but was ${result.unwrapError()}", result.unwrapError() is AppError.LocalError)
        }

    @Test
    fun `remoteResultFlow emits exactly one Success`() =
        runBlocking {
            val results = remoteResultFlow { "ok" }.toList()

            assertEquals(listOf(AsyncResult.Success("ok")), results)
        }

    @Test
    fun `remoteResultFlow emits Error for an unmapped exception`() =
        runBlocking {
            val results = remoteResultFlow<String> { throw IllegalStateException("boom") }.toList()

            assertEquals(1, results.size)
            assertTrue(results.single().unwrapError() is AppError.UnknownError)
        }

    @Test
    fun `localResultFlow forwards every emission as Success`() =
        runBlocking {
            val results = localResultFlow { flowOf(1, 2, 3) }.toList()

            assertEquals(listOf(1, 2, 3), results.map { (it as AsyncResult.Success).data })
        }

    @Test
    fun `localResultFlow emits LocalError and completes when the upstream throws`() =
        runBlocking {
            val results = localResultFlow { flow<Int> { throw IllegalStateException("disk full") } }.toList()

            assertEquals(1, results.size)
            assertTrue(results.single().unwrapError() is AppError.LocalError)
        }

    @Test
    fun `localResultFlow preserves the values emitted before the failure`() =
        runBlocking {
            val results =
                localResultFlow {
                    flow {
                        emit(1)
                        throw IllegalStateException("disk full")
                    }
                }.toList()

            assertEquals(2, results.size)
            assertEquals(1, (results[0] as AsyncResult.Success).data)
            assertTrue(results[1].unwrapError() is AppError.LocalError)
        }

    @Test
    fun `localResultFlow emits Error when building the upstream flow itself throws`() =
        runBlocking {
            val results =
                localResultFlow<Int> { throw IllegalStateException("cannot open database") }.toList()

            assertEquals(1, results.size)
            assertTrue(results.single().unwrapError() is AppError.LocalError)
        }

    @Test(expected = CancellationException::class)
    fun `remoteResult rethrows CancellationException`() {
        runBlocking { remoteResult<String> { throw CancellationException("cancelled") } }
    }

    @Test(expected = CancellationException::class)
    fun `localResult rethrows CancellationException`() {
        runBlocking { localResult<String> { throw CancellationException("cancelled") } }
    }

    @Test(expected = CancellationException::class)
    fun `remoteResultFlow rethrows CancellationException`() {
        runBlocking { remoteResultFlow<String> { throw CancellationException("cancelled") }.toList() }
    }

    @Test(expected = CancellationException::class)
    fun `localResultFlow rethrows CancellationException`() {
        runBlocking {
            localResultFlow { flow<Int> { throw CancellationException("cancelled") } }.toList()
        }
    }

    private fun flowOf(vararg values: Int): Flow<Int> = flow { values.forEach { emit(it) } }

    private fun AsyncResult<*>.unwrapError(): AppError {
        assertTrue("Expected AsyncResult.Error but was $this", this is AsyncResult.Error)
        return (this as AsyncResult.Error).error
    }
}
