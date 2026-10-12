package es.mobiledev.commonandroid.core.error

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource

/**
 * Sealed interface representing error text that can be displayed in the UI.
 * It abstracts the source of the text, allowing for both localized resources and dynamic strings.
 */
@Immutable
sealed interface UiErrorText {
    /**
     * Represents a localized string resource.
     *
     * @property id The Android string resource identifier.
     */
    data class Resource(
        @get:StringRes val id: Int,
    ) : UiErrorText

    /**
     * Represents a plain string, typically received from a remote source or generated at runtime.
     *
     * @property value The literal string content.
     */
    data class Raw(
        val value: String,
    ) : UiErrorText
}

/**
 * Resolves the [UiErrorText] into a [String] using the current Composable context.
 *
 * This method must be called within a Composable function as it relies on [stringResource]
 * to resolve [UiErrorText.Resource] variants.
 *
 * @return The resolved string value.
 */
@Composable
fun UiErrorText.resolve(): String =
    when (this) {
        is UiErrorText.Resource -> stringResource(id)
        is UiErrorText.Raw -> value
    }
