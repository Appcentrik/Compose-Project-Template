package es.mobiledev.commonandroid.core.error

import androidx.annotation.StringRes

/**
 * Encapsulates a UI action for an error, coupling its label with the execution logic.
 *
 * @property label String resource ID for the action text.
 * @property onClick Callback to be executed when the action is triggered.
 */
data class ErrorAction(
    @get:StringRes val label: Int,
    val onClick: () -> Unit,
)
