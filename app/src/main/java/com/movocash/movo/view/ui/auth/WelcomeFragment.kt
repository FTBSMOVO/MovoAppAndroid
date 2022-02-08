package com.movocash.movo.view.ui.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.LoginRequestModel
import com.movocash.movo.data.repository.BiometricInfoRepository
import com.movocash.movo.databinding.FragmentWelcomeBinding
import com.movocash.movo.persistence.tables.BiometricInfoModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.currentOS
import com.movocash.movo.utilities.extensions.showLongToastMessage
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.utilities.helper.Permissions
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.MainActivity
import com.movocash.movo.viewmodel.AccountViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.Plaid.PlaidMain
import com.movocash.movo.kycFlow.Failed_Signup
import com.movocash.movo.kycFlow.SignupFragment
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import kotlin.system.exitProcess

class WelcomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentWelcomeBinding
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(ActivityBase.activity)
    private val biometricInfoRepository = BiometricInfoRepository(ActivityBase.activity)
    private var isBioEnable = false
    var userName = ""
    var pass = ""
    val REQUEST_CODE = 333
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
       // activateReviewInfo()



        if (SDK_INT >= Build.VERSION_CODES.R) {
        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && ContextCompat.checkSelfPermission(ActivityBase.activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

           // Permissions.checkPermission(ActivityBase.activity)

          /*  if(!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(
                        String.format(
                            "package:%s",
                            *arrayOf<Any>(ActivityBase.activity.packageName)
                        )
                    )
                    startActivityForResult(intent, 2000)
                } catch (e: Exception) {
                    val obj = Intent()
                    obj.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(obj, 2000)
                }
            }*/
        } else {
            Permissions.checkPermission(ActivityBase.activity)

        }

      // Permissions.checkPermission(ActivityBase.activity)
        if (!TextUtils.isEmpty(MovoApp.db.getString(Constants.ACCESS_TOKEN)))
            accountViewModel.logoutUser()
        setListeners()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        verifyingBioMetricExistence()
    }

/*    private fun activateReviewInfo()
    {
        val manager = ReviewManagerFactory.create(MovoApp.context)


        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(ActivityBase.activity, reviewInfo)
                flow.addOnCompleteListener { task ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    if (task.isSuccessful) {

                        Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else
                        Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }



    }*/

    private fun verifyingBioMetricExistence() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                isBioEnable = true
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.ivThumb.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.ivThumb.visibility = View.GONE
            }
            else -> {
                isBioEnable = false
            }
        }
    }

        override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               // Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()

                //moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setUiObserver() {
        accountViewModel.logoutSuccess.observe(this, Observer {
            val lastUserName = MovoApp.db.getString(Constants.USER_NAME)
            val lastUserPass = MovoApp.db.getString(Constants.USER_PASS)
            val rememberUserPass = MovoApp.db.getString(Constants.REMEMBERED_USER_NAME)
            MovoApp.db.clear()
            MovoApp.db.putString(Constants.USER_NAME, lastUserName!!)
            MovoApp.db.putString(Constants.USER_PASS, lastUserPass!!)
            MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, rememberUserPass!!)
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

    private fun toTheMain() {
        val intent = Intent(ActivityBase.activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        ActivityBase.activity.finish()
    }

    private fun setListeners() {
        binding.llEnrolled.setOnClickListener(this)
        binding.btnSignIn.setOnClickListener(this)
        binding.ivThumb.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llEnrolled -> {
                //callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), "EnrollmentFragment")

                /**Calling the function to track the event when user clicks on join now to firebase*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(requireContext(), CustomEventNames.EVENT_JOIN_NOW, paramBundle)

                callFragmentWithReplace(R.id.authContainer, SignupFragment(), "EnrollmentFragment")
                /*val intent = Intent(context, PlaidMain::class.java)

                requireContext().startActivity(intent)*/

            }
            R.id.btnSignIn -> {
                callFragmentWithReplace(R.id.authContainer, SignInFragment(), "SignInFragment")
            }
            R.id.ivThumb -> {
                if (isBioEnable)
                    showBiometricDialog()
                else {
                    ActivityBase.activity.showLongToastMessage("Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint")
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


    ///////////////////////////////////////////////////
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0) {
                val storage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (storage && read) {
                    //next activity
                } else {
                    //show msg kai permission allow nahi havai
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    //msg show karo
                    //move to next activity
                } else {
                }
            }
        }
    }

/*    fun RequestPermission_Dialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", *arrayOf<Any>(ActivityBase.activity.packageName)))
                startActivityForResult(intent, 2000)
            } catch (e: Exception) {
                val obj = Intent()
                obj.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(obj, 2000)
            }
        } else {
            ActivityCompat.requestPermissions(ActivityBase.activity, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), REQUEST_CODE)
        }
    }*/

 /*   fun permission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) { // R is Android 11

            Environment.isExternalStorageManager()
        } else {
            val write = ContextCompat.checkSelfPermission(ActivityBase.activity,
                //Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(ActivityBase.activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            (write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED)
        }
    }*/

}