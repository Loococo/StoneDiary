package app.loococo.data.local.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return if (value.isNullOrBlank()) {
            emptyList()
        } else {
            value.split(",").filter { it.isNotBlank() }
        }
    }
}