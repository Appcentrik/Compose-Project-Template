package es.mobiledev.common.extensions

/**
 * Returns [message] if not blank, or the exception class name as fallback for logging.
 */
fun Throwable.messageOrTypeName(): String = message?.takeIf { it.isNotBlank() } ?: javaClass.simpleName
