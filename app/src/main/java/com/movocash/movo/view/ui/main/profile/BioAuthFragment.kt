package com.movocash.movo.view.ui.main.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.repository.BiometricInfoRepository
import com.movocash.movo.databinding.FragmentBioAuthBinding
import com.movocash.movo.persistence.local.MOVODatabase
import com.movocash.movo.persistence.tables.BiometricInfoModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class BioAuthFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentBioAuthBinding
    private val biometricManager = BiometricManager.from(ActivityBase.activity)
    private val biometricInfoRepository = BiometricInfoRepository(ActivityBase.activity)
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bio_auth, container, false)
        binding.tvTitle.isSelected = true
        setListeners()
        checkBiometricData()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        verifyingBioMetricExistence()
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.cbAuth.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.cbAuth -> {
                if (binding.cbAuth.isChecked) {
                    turnOnOffVerification(true)
                } else {
                    turnOnOffVerification(false)
                }
            }
        }
    }

    private fun turnOnOffVerification(isOn: Boolean) {
        executor = ContextCompat.getMainExecutor(ActivityBase.activity)
        biometricPrompt = BiometricPrompt(ActivityBase.activity, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        if (isOn) {
                            MovoApp.db.putBoolean(Constants.CONST_IS_THUMB_LOGIN, true)
                            MovoApp.scope.launch(Dispatchers.IO) {
                                MOVODatabase.getDatabase(ActivityBase.activity).clearAllTables()
                            }
                            biometricInfoRepository.insertAllBiometricData(BiometricInfoModel(MovoApp.db.getString(Constants.USER_NAME)!!, MovoApp.db.getString(Constants.USER_PASS)!!))
                        } else {
                            MovoApp.scope.launch(Dispatchers.IO) {
                                MovoApp.db.putBoolean(Constants.CONST_IS_THUMB_LOGIN, false)
                                MOVODatabase.getDatabase(ActivityBase.activity).clearAllTables()
                            }
                        }
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login").setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel").build()
        biometricPrompt.authenticate(promptInfo)
    }


    private fun verifyingBioMetricExistence() {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.llBiometricLogin.visibility = View.VISIBLE
                binding.tvDescription.text = "Fingerprint Unlock allows you to log in more quickly and " +
                        "easily without having to manually enter a password.\n \nYou can turn " +
                        "Fingerprint Unlock On/Off at anytime.\n\nPlease note that any Fingerprint Unlock registered on " +
                        "this device will have access to application.\n\n" +
                        "We don't recommend using Fingerprint Unlock if you are sharing your device " +
                        "with someone else."
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.tvDescription.text = "Your device doesn't support this feature"
                binding.llBiometricLogin.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.tvDescription.text = "Your device doesn't support this feature"
                binding.llBiometricLogin.visibility = View.GONE
            }
            else -> {
                binding.tvDescription.text = "To Enable this feature please go to settings menu and Enable your biometric login"
                binding.llBiometricLogin.visibility = View.GONE
            }
        }
    }

    private fun checkBiometricData() {
        if (MovoApp.db.getBoolean(Constants.CONST_IS_THUMB_LOGIN, true)) {
            var obj: BiometricInfoModel? = null
            MovoApp.scope.launch(Dispatchers.IO) {
                obj = biometricInfoRepository.getAllBiometricData()
                ActivityBase.activity.runOnUiThread {
                    if (obj != null) {
                        binding.cbAuth.isChecked = obj!!.email.equals(MovoApp.db.getString(Constants.USER_NAME), true)
                    } else {
                        binding.cbAuth.isChecked = false
                    }
                }
            }
        } else {
            binding.cbAuth.isChecked = false
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}