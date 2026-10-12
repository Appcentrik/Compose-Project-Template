package es.mobiledev.common.error

/**
 * Every failure the app can surface, already classified.
 *
 * This is the extension point every project built on this template will extend: add a case here and
 * handle it in `AppError.toUiError`, or the compiler will point at the missing branch.
 *
 * [message] is a technical description used for logging only. The copy shown to the user comes from
 * the resources `toUiError` maps each case to.
 *
 * @param message technical description of the failure, never shown to the user.
 * @param throwable the original cause, when there is one.
 */
sealed class AppError(
    open val message: String,
    open val throwable: Throwable? = null
) {
    /** The device could not reach the server at all. */
    data class NetworkError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** The request reached the server and it answered with a failure status. */
    data class ServerError(
        val code: Int? = null,
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** The server took too long to answer. Distinct from [NetworkError] so the copy can say so. */
    data class TimeoutError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** The credentials are missing, expired or insufficient. Retrying is pointless. */
    data class UnauthorizedError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** The requested resource does not exist. Retrying is pointless. */
    data class NotFoundError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** The response arrived but could not be understood. */
    data class ParseError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Reading or writing the local storage failed. */
    data class LocalError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    /** Anything not classified above. */
    data class UnknownError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)
}
