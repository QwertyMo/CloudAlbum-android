package ru.kettuproj.cloudalbum.settings

import android.content.Context
import ru.kettuproj.cloudalbum.room.AppDatabase
import ru.kettuproj.cloudalbum.room.SettingEntity

object Settings {
    private const val TOKEN = "TOKEN"

    fun getToken(context: Context):String?{
        val database = AppDatabase.getDatabase(context)
        return database.settingsDAO().getSettings().find { it.setting == TOKEN }?.value
    }

    fun setToken(context: Context, token: String){
        val database = AppDatabase.getDatabase(context)
        database.settingsDAO().update(SettingEntity(TOKEN, token))
    }

    fun deleteToken(context: Context){
        val database = AppDatabase.getDatabase(context)
        database.settingsDAO().delete(TOKEN)
    }
}