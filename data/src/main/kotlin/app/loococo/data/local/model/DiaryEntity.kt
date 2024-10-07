package app.loococo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.loococo.domain.model.Diary

@Entity(tableName = "diary")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val title: String,
    val content: String
)

fun DiaryEntity.toDiary(): Diary {
    return Diary(
        date = this.date,
        title = this.title,
        content = this.content
    )
}