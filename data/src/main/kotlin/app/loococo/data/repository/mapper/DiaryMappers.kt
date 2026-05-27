package app.loococo.data.repository.mapper

import app.loococo.data.local.room.model.DiaryEntity
import app.loococo.domain.model.Diary

/**
 * DiaryEntity(Room) ↔ Diary(Domain) 매퍼.
 * data 레이어는 도메인 의존(:domain)을 가지므로 여기서 양방향 변환을 정의한다.
 */

fun DiaryEntity.toDomain(): Diary = Diary(
    id = id,
    date = date,
    title = title,
    content = content,
    emotion = emotion,
    imageList = imageList
)

fun Diary.toEntity(): DiaryEntity = DiaryEntity(
    date = date,
    title = title,
    content = content,
    emotion = emotion,
    imageList = imageList
)
