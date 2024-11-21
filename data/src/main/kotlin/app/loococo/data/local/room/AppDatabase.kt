package app.loococo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.loococo.data.local.room.converter.Converters
import app.loococo.data.local.room.dao.DiaryDao
import app.loococo.data.local.room.model.DiaryEntity

@Database(entities = [DiaryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}