package app.loococo.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Diary(
    val id: Long = 0,
    val date: Long = 0L,
    val title: String = "",
    val content: String = "",
    val emotion: String = "",
    val imageList: List<String> = emptyList()
) {
    val localDate: LocalDate
        get() = Instant.ofEpochMilli(date)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}
