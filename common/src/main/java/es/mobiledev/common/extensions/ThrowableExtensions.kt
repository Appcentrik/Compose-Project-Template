package es.mobiledev.common.extensions

/**
 * Returns [Throwable.message] when it carries actual text, falling back to the concrete exception
 * class name otherwise.
 *
 * These messages only reach the logs, and a null or blank one hides the failure completely in a
 * crash report, whereas the class name always identifies the cause. Using a single helper keeps the
 * fallback consistent across the remote and local error handlers.
 */
fun Throwable.messageOrTypeName(): String = message?.takeIf { it.isNotBlank() } ?: javaClass.simpleName
