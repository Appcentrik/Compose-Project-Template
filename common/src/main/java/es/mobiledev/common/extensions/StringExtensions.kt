package es.mobiledev.common.extensions

import es.mobiledev.common.DATE_PATTERN_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.toSafeInt() =
    try {
        this.toInt()
    } catch (_: NumberFormatException) {
        null
    }

fun String.toSafeLong() =
    try {
        this.toLong()
    } catch (_: NumberFormatException) {
        null
    }

fun String.toLocalDate() =
    try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_PATTERN_FORMAT))
    } catch (_: DateTimeParseException) {
        null
    }
