package app.loococo.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Diary(
    val date: Long,
    val title: String,
    val content: String
) {
    val localDate: LocalDate
        get() = Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}
