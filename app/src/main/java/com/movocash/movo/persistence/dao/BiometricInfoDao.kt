package com.movocash.movo.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movocash.movo.persistence.tables.BiometricInfoModel

@Dao
interface BiometricInfoDao {

    @Query("Select * from user_biometric_info")
    fun getAllBiometricInfoData(): BiometricInfoModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBiometricInfo(obj: BiometricInfoModel)

    @Query("UPDATE user_biometric_info SET email = :email , password = :password")
    fun updateBiometricData(email: String, password: String)

    @Query("DELETE FROM user_biometric_info")
    fun deleteAll()
}