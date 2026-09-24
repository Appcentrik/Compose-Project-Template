package es.mobiledev.commonandroid.ui.component.error

import androidx.annotation.StringRes
import es.mobiledev.commonandroid.R

sealed class UiError(
    @param:StringRes open val title: Int,
    @param:StringRes open val message: Int,
    open val action: (() -> Unit)? = null,
) {
    data class Dialog(
        @param:StringRes override val title: Int,
        @param:StringRes override val message: Int,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Sheet(
        @param:StringRes override val title: Int,
        @param:StringRes override val message: Int,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Screen(
        @param:StringRes override val title: Int,
        @param:StringRes override val message: Int,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class SnackBar(
        @param:StringRes override val title: Int,
        @param:StringRes override val message: Int,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data class Embedded(
        @param:StringRes override val title: Int,
        @param:StringRes override val message: Int,
        override val action: (() -> Unit)? = null,
    ) : UiError(title, message, action)

    data object None : UiError(
        title = R.string.empty_string,
        message = R.string.empty_string,
    )
}
