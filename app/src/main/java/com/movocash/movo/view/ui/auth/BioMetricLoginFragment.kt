package com.movocash.movo.view.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.LoginRequestModel
import com.movocash.movo.data.repository.BiometricInfoRepository
import com.movocash.movo.databinding.FragmentBiometricLoginBinding
import com.movocash.movo.persistence.tables.BiometricInfoModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.currentOS
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.MainActivity
import com.movocash.movo.viewmodel.AccountViewModel

class BioMetricLoginFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentBiometricLoginBinding
    private val biometricInfoRepository = BiometricInfoRepository(ActivityBase.activity)
    private lateinit var authViewModel: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_biometric_login, container, false)
        authViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUIObserver()
        setListener()
        return binding.root
    }

    private fun setUIObserver() {
        authViewModel.loginResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                biometricInfoRepository.insertAllBiometricData(
                    BiometricInfoModel(
                        binding.edEmail.text.toString(),
                        binding.edPass.text.toString()
                    )
                )
                MovoApp.db.putString(Constants.FIRST_NAME, obj.data!!.firstName)
                MovoApp.db.putString(Constants.LAST_NAME, obj.data!!.lastName)
                MovoApp.db.putString(Constants.ACCESS_TOKEN, obj.data!!.token)
                MovoApp.db.putString(Constants.USER_ID, obj.data!!.userId)
                MovoApp.db.putString(Constants.EMAIL, obj.data!!.email)
                MovoApp.db.putString(
                    Constants.USER_NUMBER,
                    obj.data!!.accountInfo!!.cellPhoneNumber
                )
                MovoApp.db.putString(Constants.USER_NAME, binding.edEmail.text.toString())
                MovoApp.db.putString(Constants.USER_PASS, binding.edPass.text.toString())
                MovoApp.db.putString(Constants.USER_IMAGE, obj.data!!.profilePicture)
                MovoApp.db.putString(Constants.USER_THUMB, obj.data!!.profilePictureThumb)
                if (obj.data!!.lastLogin != null && !TextUtils.isEmpty(obj.data!!.lastLogin!!))
                    MovoApp.db.putString(Constants.USER_LAST_LOGIN, obj.data!!.lastLogin!!)
                MovoApp.db.putString(Constants.CONST_LOGIN_RESPONSE, Gson().toJson(obj))

                /**Calling the function to track the login event to firebase*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_SIGN_IN,
                    paramBundle
                )

                toTheMain()
            }
        })

        authViewModel.loginFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
            }
        })
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener(this)
        binding.btnSignIn.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.edEmail.text.toString())) {
            binding.edEmail.requestFocus()
            binding.edEmail.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.edPass.text.toString())) {
            binding.edPass.requestFocus()
            binding.edPass.errorAnim(ActivityBase.activity)
            return false
        } else
            return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnSignIn -> {
                if (validateInput()) {
                    MovoApp.db.putBoolean(Constants.CONST_IS_THUMB_LOGIN, true)
                    val deviceType = 1
                    val deviceModel: String = Build.MANUFACTURER + " " + Build.MODEL
                    val os: String = currentOS()
                    val version: String = Build.VERSION.RELEASE
                    authViewModel.loginUser(LoginRequestModel(binding.edEmail.text.toString(), binding.edPass.text.toString(), MovoApp.db.getString(Constants.SESSION_ID)!!, MovoApp.db.getString(Constants.SESSION_ID_STATUS)!!, "", deviceModel, os, version, deviceType))
                }
            }
        }

    }

    private fun toTheMain() {
        val intent = Intent(ActivityBase.activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        ActivityBase.activity.finish()
    }
}