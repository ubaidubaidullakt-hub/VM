package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.VirtualAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VirtualAppDao {
    @Query("SELECT * FROM virtual_apps ORDER BY isSystemApp DESC, appName ASC")
    fun getAllApps(): Flow<List<VirtualAppEntity>>

    @Query("SELECT * FROM virtual_apps WHERE isRootGranted = 1")
    fun getRootGrantedApps(): Flow<List<VirtualAppEntity>>

    @Query("SELECT * FROM virtual_apps WHERE packageName = :packageName LIMIT 1")
    suspend fun getAppByPackage(packageName: String): VirtualAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: VirtualAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<VirtualAppEntity>)

    @Update
    suspend fun updateApp(app: VirtualAppEntity)

    @Query("DELETE FROM virtual_apps WHERE packageName = :packageName")
    suspend fun deleteApp(packageName: String)

    @Query("UPDATE virtual_apps SET isRootGranted = :granted WHERE packageName = :packageName")
    suspend fun updateRootAccess(packageName: String, granted: Boolean)

    @Query("UPDATE virtual_apps SET isRunning = :running WHERE packageName = :packageName")
    suspend fun updateRunningState(packageName: String, running: Boolean)

    @Query("UPDATE virtual_apps SET isRunning = 0")
    suspend fun stopAllApps()
}
