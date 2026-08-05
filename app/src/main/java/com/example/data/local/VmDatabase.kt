package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.RootLogDao
import com.example.data.local.dao.VirtualAppDao
import com.example.data.local.dao.VmProfileDao
import com.example.data.local.entities.RootLogEntity
import com.example.data.local.entities.VirtualAppEntity
import com.example.data.local.entities.VmProfileEntity

@Database(
    entities = [VirtualAppEntity::class, VmProfileEntity::class, RootLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VmDatabase : RoomDatabase() {
    abstract fun virtualAppDao(): VirtualAppDao
    abstract fun vmProfileDao(): VmProfileDao
    abstract fun rootLogDao(): RootLogDao

    companion object {
        @Volatile
        private var INSTANCE: VmDatabase? = null

        fun getInstance(context: Context): VmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VmDatabase::class.java,
                    "droid_vm_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
