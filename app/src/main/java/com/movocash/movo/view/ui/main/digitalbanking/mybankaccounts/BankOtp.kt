package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.movocash.movo.R
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.view.ui.auth.EnrollmentFragment.Companion.binding
import com.movocash.movo.view.ui.base.ActivityBase

class BankOtp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_otp)


    }



    private fun validateInput(): Boolean {


        return true;

    }



}