package com.movocash.movo.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.movocash.movo.MovoApp
import com.movocash.movo.persistence.local.MOVODatabase
import com.movocash.movo.persistence.tables.BiometricInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiometricInfoRepository(private val context: Context) {

    @WorkerThread
    fun insertAllBiometricData(obj: BiometricInfoModel) {
        MovoApp.scope.launch(Dispatchers.IO) {
            MOVODatabase.getDatabase(context).biometricInfoDao().insertAllBiometricInfo(obj = obj)
        }
    }

    @WorkerThread
    fun getAllBiometricData(): BiometricInfoModel {
        return MOVODatabase.getDatabase(context).biometricInfoDao().getAllBiometricInfoData()
    }

    @WorkerThread
    fun updateBiometricData(email: String, password: String) {
        MovoApp.scope.launch(Dispatchers.IO) {
            MOVODatabase.getDatabase(context).biometricInfoDao().updateBiometricData(email, password)
        }
    }


}