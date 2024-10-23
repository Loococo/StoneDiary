package app.loococo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.loococo.data.local.converter.Converters
import app.loococo.data.local.dao.DiaryDao
import app.loococo.data.local.model.DiaryEntity

@Database(entities = [DiaryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}