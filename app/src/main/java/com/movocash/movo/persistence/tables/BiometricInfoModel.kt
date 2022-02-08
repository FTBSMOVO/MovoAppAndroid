package com.movocash.movo.persistence.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_biometric_info")
class BiometricInfoModel {

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
    var email : String  = ""
    var password : String = ""

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}