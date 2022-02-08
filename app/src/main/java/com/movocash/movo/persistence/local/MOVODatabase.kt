package com.movocash.movo.persistence.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.movocash.movo.persistence.dao.BiometricInfoDao
import com.movocash.movo.persistence.tables.BiometricInfoModel

@Database(entities = [(BiometricInfoModel::class)], version = 1, exportSchema = false)
abstract class MOVODatabase : RoomDatabase() {

    abstract fun biometricInfoDao(): BiometricInfoDao

    companion object {
        @Volatile
        private var INSTANCE: MOVODatabase? = null

        fun getDatabase(context: Context): MOVODatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, MOVODatabase::class.java, "movo.db").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}