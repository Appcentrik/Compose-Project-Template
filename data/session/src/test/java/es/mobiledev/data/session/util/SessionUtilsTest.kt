package es.mobiledev.data.session.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class SessionUtilsTest {
    private val key = longPreferencesKey("test_key")
    private val defaultValue = -1L

    @Test
    fun `returns the stored value when the key exists`() =
        runTest {
            val store = FakeDataStore(flowOf(preferencesOf(key to 42L)))

            assertEquals(42L, store.getPreference(key, defaultValue))
        }

    @Test
    fun `returns the default when the key is absent`() =
        runTest {
            val store = FakeDataStore(flowOf(emptyPreferences()))

            assertEquals(defaultValue, store.getPreference(key, defaultValue))
        }

    @Test
    fun `returns the default when the read fails with an IOException`() =
        runTest {
            val store = FakeDataStore(flow { throw IOException("disk gone") })

            assertEquals(defaultValue, store.getPreference(key, defaultValue))
        }

    @Test
    fun `rethrows a failure that is not an IOException`() =
        runTest {
            val store = FakeDataStore(flow { throw IllegalStateException("boom") })

            val failure =
                runCatching {
                    store.getPreference(key, defaultValue)
                }.exceptionOrNull()

            assertTrue("Expected IllegalStateException but was $failure", failure is IllegalStateException)
        }

    private class FakeDataStore(
        private val source: Flow<Preferences>,
    ) : DataStore<Preferences> {
        override val data: Flow<Preferences> = source

        override suspend fun updateData(
            transform: suspend (t: Preferences) -> Preferences,
        ): Preferences = throw UnsupportedOperationException("this fake only reads")
    }
}
