package com.movocash.movo.view.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.movocash.movo.Plaid.PlaidMain
import com.movocash.movo.R
import com.movocash.movo.databinding.ActivityAuthBinding
import com.movocash.movo.view.ui.base.ActivityBase

class AuthActivity : ActivityBase() {

    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        setStatusBarColor(ContextCompat.getColor(this, R.color.white), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        callFragment(R.id.authContainer, WelcomeFragment(), null)

       /* val intent = Intent(this, PlaidMain::class.java)

        startActivity(intent)*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val fragment = supportFragmentManager.findFragmentById(R.id.authContainer)) {
            is EnrollmentFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
            is VerifyCodeFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
        }
    }
}