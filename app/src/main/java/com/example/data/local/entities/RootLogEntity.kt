package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "root_logs")
data class RootLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val requestingPackage: String,
    val appName: String,
    val commandExecuted: String,
    val actionTaken: String, // GRANTED, DENIED, REVOKED
    val timestamp: Long = System.currentTimeMillis()
)
