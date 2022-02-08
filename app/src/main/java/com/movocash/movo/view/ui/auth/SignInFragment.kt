package com.movocash.movo.view.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.LoginRequestModel
import com.movocash.movo.data.model.responsemodel.GetFrequesnciesResponseModel
import com.movocash.movo.data.model.responsemodel.sideMenuResponseModel
import com.movocash.movo.data.repository.BiometricInfoRepository
import com.movocash.movo.databinding.FragmentSignInBinding
import com.movocash.movo.persistence.tables.BiometricInfoModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.MediaBottomSheetFragment
import com.movocash.movo.view.ui.main.MainActivity
import com.movocash.movo.view.ui.main.cashcard.AutoReloads
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class SignInFragment : BaseFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    lateinit var binding: FragmentSignInBinding
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(ActivityBase.activity)
    private val biometricInfoRepository = BiometricInfoRepository(ActivityBase.activity)
    private var isBioEnable = false
    var userName = ""
    var pass = ""

    private lateinit var mListener: ISelectListener

    private var configurationList: ArrayList<sideMenuResponseModel.AvailableSubMenu> = ArrayList()

    private var disableList: ArrayList<sideMenuResponseModel.AvailableSubMenu> = ArrayList()
    private var submenu: ArrayList<sideMenuResponseModel.AvailableSubMenu> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        setUiObserver()
        setViews()
        commonViewModel.getConfigurations()
        setListeners()
        //showStylishDialog("You've  Just Downloaded a Powerful Fintech App with Security that Matters.",getString(R.string.msg1),getText(R.string.msg2).toString())


        if(configurationList!=null)
        {
            for (i in configurationList) {

                if(i.availableSubMenu!=null)
                {
                    configurationList.addAll(i.availableSubMenu!!)
                }

                Log.d("sidemenu", i.name.toString())
            }
        }

        return binding.root
    }



    private fun setViews() {
        MovoApp.initThreatMatrix()
        if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.REMEMBERED_USER_NAME))) {
            binding.etEmail.setText("${MovoApp.db.getString(Constants.REMEMBERED_USER_NAME)}")
            binding.cbRemember.isChecked = true
        }
    }

    override fun onResume() {
        super.onResume()
        verifyingBioMetricExistence()
    }

    private fun verifyingBioMetricExistence() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                isBioEnable = true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.ivThumb.visibility = View.GONE
                binding.rlOrSignIn.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.ivThumb.visibility = View.GONE
                binding.rlOrSignIn.visibility = View.GONE
            }
            else -> {
                isBioEnable = false
            }
        }
    }

    private fun setUiObserver() {

        commonViewModel.sideMenuResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->


                // obj.data!!.availableSubMenu!![3].id

                if (obj.data!!.availableSubMenu != null && obj.data!!.availableSubMenu!!.size > 0) {
                    if (configurationList.size > 0)
                        configurationList.clear()
                    configurationList.addAll(obj.data!!.availableSubMenu!!)





                        for (i in configurationList) {

                            if(i.availableSubMenu!=null)
                            {
                                submenu.addAll(i.availableSubMenu!!)
                            }
                            if(i.id==3)
                            {
                                Constants.MOVO_CASH = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.MOVO_CASH, i.isEnabled.toString())
                            }
                            if(i.id==4)
                            {
                                Constants.MOVO_PAY = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.MOVO_PAY, i.isEnabled.toString())
                            }
                            if(i.id==5)
                            {
                                Constants.CASH_CARD = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.CASH_CARD, i.isEnabled.toString())
                            }
                            if(i.id==6)
                            {
                                Constants.MOVO_CHAIN = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.MOVO_CHAIN, i.isEnabled.toString())
                            }
                            if(i.id==7)
                            {
                                Constants.DIGITAL_BANKING = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.DIGITAL_BANKING, i.isEnabled.toString())
                            }
                            if(i.id==8)
                            {
                                Constants.ECHECK_BOOK = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.ECHECK_BOOK, i.isEnabled.toString())
                            }
                            if(i.id==9)
                            {
                                Constants.MY_PROFILE = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.MY_PROFILE, i.isEnabled.toString())
                            }
                            if(i.id==10)
                            {
                                Constants.LOCK_UNLOCK_CARD = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.LOCK_UNLOCK_CARD, i.isEnabled.toString())
                            }
                            if(i.id==11)
                            {
                                Constants.CHANGE_PASSWORD = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.CHANGE_PASSWORD, i.isEnabled.toString())
                            }
                            if(i.id==12)
                            {
                                Constants.DEPOSIT_HUB = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.DEPOSIT_HUB, i.isEnabled.toString())
                            }
                            if(i.id==13)
                            {
                                Constants.ABOUT_US = i.isEnabled.toString()
                                MovoApp.db.putString(Constants.ABOUT_US, i.isEnabled.toString())
                            }

                            Log.d("sidemenu", i.name.toString())
                        }

                    for (i in submenu) {

                        if(i.appId=="m_accounts")
                        {
                            Constants.m_Accounts = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.m_Accounts, i.isEnabled.toString())
                        }
                        if(i.appId=="m_accountsummary")
                        {
                            Constants.M_ACCOUNTS_SUMMARY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_ACCOUNTS_SUMMARY, i.isEnabled.toString())
                        }
                        if(i.appId=="m_orderphysical")
                        {
                            Constants.M_ORDER_PHYSICAL_CARD = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_ORDER_PHYSICAL_CARD, i.isEnabled.toString())
                        }
                        ////////////////////////////////////////////////////////////
                        if(i.appId=="m_sendmoney")
                        {
                            Constants.M_SEND_MONEY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SEND_MONEY, i.isEnabled.toString())
                        }
                        if(i.appId=="m_sendhistory")
                        {
                            Constants.M_HISTORY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_HISTORY, i.isEnabled.toString())
                        }

                        /////////////////////////////////////////////////////////////
                        if(i.appId=="m_mybankaccounts")
                        {
                            Constants.M_MY_BANK_ACCOUNTS = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_MY_BANK_ACCOUNTS, i.isEnabled.toString())
                        }
                        if(i.appId=="m_achout")
                        {
                            Constants.M_CASH_OUT_TO_BANK = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_CASH_OUT_TO_BANK, i.isEnabled.toString())
                        }
                        if(i.appId=="m_achload")
                        {
                            Constants.M_CASH_IN_TO_MOVO = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_CASH_IN_TO_MOVO, i.isEnabled.toString())
                        }
                        if(i.appId=="m_directdeposit")
                        {
                            Constants.M_DIRECT_DEPOSIT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_DIRECT_DEPOSIT, i.isEnabled.toString())
                        }
                        if(i.appId=="m_achdirect")
                        {
                            Constants.M_ACH_TRANSFERS = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_ACH_TRANSFERS, i.isEnabled.toString())
                        }
                        if(i.appId=="m_scheduledach")
                        {
                            Constants.M_SCHEDULED_TRANSFER = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SCHEDULED_TRANSFER, i.isEnabled.toString())
                        }
                        if(i.appId=="m_achhistory")
                        {
                            Constants.M_Transfer_ACTIVITY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_Transfer_ACTIVITY, i.isEnabled.toString())
                        }
                        ////////////////////////////E-CHECKBOOK///////////////////

                        if(i.appId=="m_makepayment")
                        {
                            Constants.M_MAKEPAYMENT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_MAKEPAYMENT, i.isEnabled.toString())
                        }
                        if(i.appId=="m_billpayhistory")
                        {
                            Constants.M_PAYMENT_HISTORY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_PAYMENT_HISTORY, i.isEnabled.toString())
                        }
                        if(i.appId=="m_addbillers")
                        {
                            Constants.M_ADD_PAYEES = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_ADD_PAYEES, i.isEnabled.toString())
                        }
                        if(i.appId=="m_mybillers")
                        {
                            Constants.M_MY_PAYEES= i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_MY_PAYEES, i.isEnabled.toString())
                        }
                        if(i.appId=="m_scheduledbillpay")
                        {
                            Constants.M_SCHEDULED_PAYMENT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SCHEDULED_PAYMENT, i.isEnabled.toString())
                        }

                        ////////////////////MY PROFILE?SETTINGS//////////////////

                        if(i.appId=="m_achhistory")
                        {
                            Constants.M_SCHEDULED_TRANSFER = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SCHEDULED_TRANSFER, i.isEnabled.toString())
                        }
                        ////////////////////////////E-CHECKBOOK///////////////////

                        if(i.appId=="m_manageprofile")
                        {
                            Constants.M_MAKEPAYMENT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_MY_BANK_ACCOUNTS, i.isEnabled.toString())
                        }
                        if(i.appId=="m_alerts")
                        {
                            Constants.M_PAYMENT_HISTORY = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_CASH_OUT_TO_BANK, i.isEnabled.toString())
                        }
                        if(i.appId=="m_biometric")
                        {
                            Constants.M_ADD_PAYEES = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_ACH_TRANSFERS, i.isEnabled.toString())
                        }
                        if(i.appId=="m_support")
                        {
                            Constants.M_MY_PAYEES= i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_DIRECT_DEPOSIT, i.isEnabled.toString())
                        }
                        if(i.appId=="m_terms")
                        {
                            Constants.M_SCHEDULED_PAYMENT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SCHEDULED_TRANSFER, i.isEnabled.toString())
                        }
                        if(i.appId=="m_privacy")
                        {
                            Constants.M_SCHEDULED_PAYMENT = i.isEnabled.toString()
                            MovoApp.db.putString(Constants.M_SCHEDULED_TRANSFER, i.isEnabled.toString())
                        }

                    }

                        /*if(i.availableSubMenu!=null)
                        {
                            submenu.addAll(i.availableSubMenu!!)
                        }

                        Log.d("sidemenu", i.name.toString())
                    }*/

                /*for (i in submenu) {



                    Log.d("sidemenu", i.name.toString())
                }*/

                 /*   for (i in configurationList) {

                        if(i.availableSubMenu!=null)
                        {
                            configurationList.addAll(i.availableSubMenu!!)
                        }

                        Log.d("sidemenu", i.name.toString())
                    }*/

                    /*for (i in configurationList) {



                        Log.d("sidemenu", i.name.toString())
                    }*/

                    disableList = configurationList.filter { it.isEnabled==false } as ArrayList<sideMenuResponseModel.AvailableSubMenu>

                }

                //ActivityBase.activity.showToastMessage("ID : " + configurationList[3].id)
               // MovoApp.db.putString(Constants.menu, configurationList[3].id.toString())

            }


        })
        commonViewModel.sideMenuFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
            }
        })

        accountViewModel.loginFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
            }
        })

        accountViewModel.loginResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                MovoApp.db.putString(Constants.FIRST_NAME, obj.data!!.firstName)
                MovoApp.db.putString(Constants.USER_NAME, userName)
                MovoApp.db.putString(Constants.USER_PASS, pass)
                MovoApp.db.putString(Constants.LAST_NAME, obj.data!!.lastName)
                MovoApp.db.putString(Constants.USER_IMAGE, obj.data!!.profilePicture)
                MovoApp.db.putString(Constants.USER_THUMB, obj.data!!.profilePictureThumb)
                MovoApp.db.putString(Constants.ACCESS_TOKEN, obj.data!!.token)
                MovoApp.db.putString(Constants.EMAIL, obj.data!!.email)
                MovoApp.db.putString(Constants.USER_NUMBER, obj.data!!.accountInfo!!.cellPhoneNumber)
                MovoApp.db.putString(Constants.USER_ID, obj.data!!.userId)
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
    }

    private fun setListeners() {
        binding.tvForgotPass.setOnClickListener(this)
        binding.btnSignIn.setOnClickListener(this)
        binding.ivThumb.setOnClickListener(this)
        binding.cbRemember.setOnCheckedChangeListener(this)
        binding.tvCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvCancel ->
            {
                callFragmentWithReplace(R.id.authContainer, WelcomeFragment(), "SignInFragment")
                //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }

            R.id.tvForgotPass -> {
                addFragment(R.id.authContainer, SendCodeFragment.newInstance(false), "SendCodeFragment")
               // callFragmentWithReplace(R.id.authContainer, AutoReloads(), "SignInFragment")
/*
                if(configurationList!=null)
                {
                    for (i in configurationList) {

                        if(i.availableSubMenu!=null)
                        {
                            submenu.addAll(i.availableSubMenu!!)
                        }

                        Log.d("sidemenu", i.name.toString())
                    }
                }
                for (i in submenu) {



                    Log.d("sidemenu", i.name.toString())
                }*/
            }
            R.id.ivThumb -> {
                if (isBioEnable)
                    showBiometricDialog()
                else {
                    ActivityBase.activity.showLongToastMessage("Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint")
                }
            }
            R.id.btnSignIn -> {
                MovoApp.db.putBoolean(Constants.CONST_IS_THUMB_LOGIN, false)
                if (validateInput()) {
                    loginUser()



                }
            }
        }
    }

    private fun showBiometricDialog() {
        executor = ContextCompat.getMainExecutor(ActivityBase.activity)
        biometricPrompt = BiometricPrompt(ActivityBase.activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        var obj: BiometricInfoModel? = null
                        MovoApp.scope.launch(Dispatchers.IO) {
                            obj = biometricInfoRepository.getAllBiometricData()
                            activity!!.runOnUiThread {
                                if (obj != null) {
                                    MovoApp.db.putBoolean(Constants.CONST_IS_THUMB_LOGIN, true)
                                    val deviceType = 1
                                    val deviceModel: String = Build.MANUFACTURER + " " + Build.MODEL
                                    val os: String = currentOS()
                                    val version: String = Build.VERSION.RELEASE
                                    userName = obj!!.email
                                    pass = obj!!.password
                                    accountViewModel.loginUser(LoginRequestModel(obj!!.email, obj!!.password, MovoApp.db.getString(Constants.SESSION_ID)!!, MovoApp.db.getString(Constants.SESSION_ID_STATUS)!!, "", deviceModel, os, version, deviceType))
                                } else {
                                    addFragment(R.id.authContainer, BioMetricLoginFragment(), "BiometricLoginFragment")
                                }
                            }
                        }
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login").setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel").setConfirmationRequired(false).build()
        biometricPrompt.authenticate(promptInfo)

    }

    private fun loginUser() {
        val deviceType = 1
        val deviceModel: String = Build.MANUFACTURER + " " + Build.MODEL
        val os: String = currentOS()
        val version: String = Build.VERSION.RELEASE
        userName = binding.etEmail.text.toString()
        pass = binding.etPass.text.toString()
        accountViewModel.loginUser(LoginRequestModel(binding.etEmail.text.toString(), binding.etPass.text.toString(), MovoApp.db.getString(Constants.SESSION_ID)!!, MovoApp.db.getString(Constants.SESSION_ID_STATUS)!!, "", deviceModel, os, version, deviceType))
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            binding.etEmail.requestFocus()
            binding.etEmail.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etPass.text.toString())) {
            binding.etPass.requestFocus()
            binding.etPass.errorAnim(ActivityBase.activity)
            return false
        } else
            return true
    }

    private fun toTheMain() {
        val intent = Intent(ActivityBase.activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        ActivityBase.activity.finish()
    }

    override fun onCheckedChanged(p0: CompoundButton?, isCheck: Boolean) {
        if (isCheck) {
            MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, binding.etEmail.text.toString())
        } else {
            MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, "")
        }
    }

    interface UpdateButtonListener {
        fun onUpdate(status: Boolean)
    }

    fun setMyListener(listener: ISelectListener) {
        mListener = listener
    }

    interface ISelectListener {
        //fun onMediaSelect(pos: Int)
        fun onMediaSelect(value:Boolean)
        fun onMediaCancel()
    }

}