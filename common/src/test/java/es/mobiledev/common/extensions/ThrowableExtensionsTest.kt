package es.mobiledev.common.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

private class CustomRemoteException : IOException()

class ThrowableExtensionsTest {
    @Test
    fun `returns the message when it has text`() {
        assertEquals("database is locked", IOException("database is locked").messageOrTypeName())
    }

    @Test
    fun `falls back to the class name when the message is null`() {
        assertEquals("IOException", IOException().messageOrTypeName())
    }

    @Test
    fun `falls back to the class name when the message is blank`() {
        assertEquals("SocketTimeoutException", SocketTimeoutException().messageOrTypeName())
        assertEquals("SocketTimeoutException", SocketTimeoutException("   ").messageOrTypeName())
    }

    @Test
    fun `uses the runtime class name and not the declared one`() {
        val error: IOException = CustomRemoteException()

        assertEquals("CustomRemoteException", error.messageOrTypeName())
    }
}
