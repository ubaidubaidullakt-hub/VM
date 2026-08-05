package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.VmProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VmProfileDao {
    @Query("SELECT * FROM vm_profiles WHERE instanceId = :id LIMIT 1")
    fun getProfileFlow(id: String): Flow<VmProfileEntity?>

    @Query("SELECT * FROM vm_profiles WHERE instanceId = :id LIMIT 1")
    suspend fun getProfile(id: String): VmProfileEntity?

    @Query("SELECT * FROM vm_profiles ORDER BY createdTimestamp DESC")
    fun getAllProfiles(): Flow<List<VmProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: VmProfileEntity)

    @Update
    suspend fun updateProfile(profile: VmProfileEntity)

    @Query("DELETE FROM vm_profiles WHERE instanceId = :id")
    suspend fun deleteProfile(id: String)
}
