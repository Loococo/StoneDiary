package app.loococo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.loococo.data.local.dao.DiaryDao
import app.loococo.data.local.model.DiaryEntity

@Database(entities = [DiaryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}