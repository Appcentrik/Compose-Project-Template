package es.mobiledev.common.error

sealed class AppError(
    open val message: String,
    open val throwable: Throwable? = null
) {
    data class ServerError(
        val code: Int? = null,
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    data class NetworkError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    data class ParseError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    data class LocalError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)

    data class UnknownError(
        override val message: String,
        override val throwable: Throwable? = null
    ) : AppError(message, throwable)
}
