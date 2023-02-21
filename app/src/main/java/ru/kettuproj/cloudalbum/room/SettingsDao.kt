package ru.kettuproj.cloudalbum.room

import androidx.room.*

@Dao
interface SettingsDao {
    @Query("SELECT * FROM SettingEntity")
    fun getSettings():List<SettingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(setting: SettingEntity)

    @Query("DELETE FROM SettingEntity WHERE setting IN (:setting)")
    fun delete(setting: String)
}