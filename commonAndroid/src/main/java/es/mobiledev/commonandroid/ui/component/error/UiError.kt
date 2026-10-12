package es.mobiledev.commonandroid.ui.component.error

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

/**
 * Presentation agnostic description of an error, ready to be rendered by any of the components in
 * this package.
 *
 * It is a plain value on purpose: it holds no lambda, so it keeps a meaningful `equals`/`hashCode` and
 * can safely live inside a `data class UiState` without breaking the distinct-until-changed
 * behaviour of a StateFlow.
 *
 * @property title headline of the error.
 * @property message body of the error.
 * @property actionLabel label of the optional action button, `null` to render no action.
 */
@Immutable
sealed interface UiError {
    @get:StringRes
    val title: Int

    @get:StringRes
    val message: Int

    @get:StringRes
    val actionLabel: Int?

    /** Blocking errors replace the content of the screen, snackbars are transient overlays on top of it. */
    val isBlocking: Boolean get() = this !is SnackBar

    /**
     * Same error with no action at all.
     *
     * The action label and the action itself live apart: the label is part of this value while the
     * behaviour is held by the ViewModel. Without this, an error built with the default retry label
     * but with no bound action would render a button that does nothing and would never auto dismiss.
     */
    fun withoutAction(): UiError

    data class Dialog(
        @get:StringRes override val title: Int,
        @get:StringRes override val message: Int,
        @get:StringRes override val actionLabel: Int? = null,
    ) : UiError {
        override fun withoutAction(): UiError =
            if (actionLabel == null) {
                this
            } else {
                copy(actionLabel = null)
            }
    }

    data class Sheet(
        @get:StringRes override val title: Int,
        @get:StringRes override val message: Int,
        @get:StringRes override val actionLabel: Int? = null,
    ) : UiError {
        override fun withoutAction(): UiError =
            if (actionLabel == null) {
                this
            } else {
                copy(actionLabel = null)
            }
    }

    data class Screen(
        @get:StringRes override val title: Int,
        @get:StringRes override val message: Int,
        @get:StringRes override val actionLabel: Int? = null,
    ) : UiError {
        override fun withoutAction(): UiError =
            if (actionLabel == null) {
                this
            } else {
                copy(actionLabel = null)
            }
    }

    data class SnackBar(
        @get:StringRes override val title: Int,
        @get:StringRes override val message: Int,
        @get:StringRes override val actionLabel: Int? = null,
    ) : UiError {
        override fun withoutAction(): UiError =
            if (actionLabel == null) {
                this
            } else {
                copy(actionLabel = null)
            }
    }

    data class Embedded(
        @get:StringRes override val title: Int,
        @get:StringRes override val message: Int,
        @get:StringRes override val actionLabel: Int? = null,
    ) : UiError {
        override fun withoutAction(): UiError =
            if (actionLabel == null) {
                this
            } else {
                copy(actionLabel = null)
            }
    }
}
