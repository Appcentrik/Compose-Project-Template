package es.mobiledev.data.session.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Saves a preference to the DataStore.
 * @param key The preference key.
 * @param value The value to save.
 */
suspend fun <T> DataStore<Preferences>.savePreference(
    key: Preferences.Key<T>,
    value: T
) {
    this.edit { prefs ->
        prefs[key] = value
    }
}

/**
 * Retrieves a preference from the DataStore.
 * @param key The preference key.
 * @param defaultValue The default value to return if the preference is not found.
 * @return The preference value, or the default value if not found.
 */
suspend fun <T> DataStore<Preferences>.getPreference(
    key: Preferences.Key<T>,
    defaultValue: T
): T =
    this.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }.map { prefs ->
            prefs[key] ?: defaultValue
        }.first()

/**
 * Removes a preference from the DataStore.
 * @param key The preference key to remove.
 */
suspend fun <T> DataStore<Preferences>.removePreference(
    key: Preferences.Key<T>
) {
    this.edit { prefs ->
        prefs.remove(key)
    }
}

/**
 * Clears all preferences from the DataStore.
 */
suspend fun DataStore<Preferences>.clearAllPreferences() {
    this.edit { prefs ->
        prefs.clear()
    }
}

/**
 * Observes a preference in the DataStore.
 * @param key The preference key.
 * @param defaultValue The default value to return if the preference is not found.
 * @return A Flow that emits the preference value whenever it changes.
 */
fun <T> DataStore<Preferences>.observePreference(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> = this.data.map { prefs -> prefs[key] ?: defaultValue }
