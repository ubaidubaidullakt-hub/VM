package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.RootLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RootLogDao {
    @Query("SELECT * FROM root_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<RootLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: RootLogEntity)

    @Query("DELETE FROM root_logs")
    suspend fun clearLogs()
}
