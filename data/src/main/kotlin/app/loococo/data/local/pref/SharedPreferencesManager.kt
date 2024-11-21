package app.loococo.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson


class SharedPreferencesManager(context: Context) {

    private val gson by lazy {
        Gson().newBuilder().create()
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("StonePreferences", Context.MODE_PRIVATE)

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun <T> saveObject(key: String, value: T) {
        sharedPreferences.edit().putString(key, gson.toJson(value)).apply()
    }

    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            gson.fromJson(json, classOfT)
        } else {
            null
        }
    }
}