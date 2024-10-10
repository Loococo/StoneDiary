package app.loococo.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formattedHomeDate(): String {
    return format(DateTimeFormatter.ofPattern("yyyy.MM"))
}

fun LocalDate.formattedDateWrite(): String {
    return format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E"))
}