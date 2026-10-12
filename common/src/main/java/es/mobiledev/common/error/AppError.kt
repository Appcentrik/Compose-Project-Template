package es.mobiledev.common.error

/**
 * Classified application error.
 *
 * @param message Technical description for logging only (never shown to user).
 * @param throwable Original cause, if any.
 */
sealed class AppError(
    open val message: String,
    open val throwable: Throwable? = null,
) {
    /** Network connection failure. */
    data class NetworkError(
        override val message: String,
        override val throwable: Throwable? = null,
    ) : AppError(message, throwable)

    /** Server response failure. */
    data class ServerError(
        val code: Int? = null,
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Request timeout. */
    data class TimeoutError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Authentication or authorization failure. */
    data class UnauthorizedError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Requested resource not found. */
    data class NotFoundError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Failed to parse response data. */
    data class ParseError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Local storage failure. */
    data class LocalError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Unclassified error. */
    data class UnknownError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)
}
