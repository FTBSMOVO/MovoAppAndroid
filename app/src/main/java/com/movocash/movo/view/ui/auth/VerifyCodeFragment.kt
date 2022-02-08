package com.movocash.movo.view.ui.auth

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ForgotPasswordRequestModel
import com.movocash.movo.data.model.requestmodel.SendVerificationCodeRequestModel
import com.movocash.movo.databinding.FragmentNewPasswordBinding
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.AccountViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class VerifyCodeFragment : BaseFragment(), View.OnClickListener, TextWatcher, IInfoListener {

    lateinit var binding: FragmentNewPasswordBinding
    private lateinit var accountViewModel: AccountViewModel
    private var countDownTimer: CountDownTimer? = null
    private var isLength = false
    private var isUp = false
    private var isLower = false
    private var isSpecial = false
    private var isNum = false
    private var isShow = false
    private var isConfirmShow = false
    private val SMS_CONSENT_REQUEST = 2  // Set to an unused request code
    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        binding.root.hideKeyboard()
                        Log.e("sms", "2_sms")
                        val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            ActivityBase.activity.startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                        } catch (e: ActivityNotFoundException) {

                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {

                    }
                }
            }
        }
    }

    companion object {
        lateinit var instance: VerifyCodeFragment
        lateinit var userName: String

        fun newInstance(name: String): VerifyCodeFragment {
            instance = VerifyCodeFragment()
            userName = name
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_password, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setListeners()
        startTimer()
        startSMSListener()
        return binding.root
    }

    private fun startSMSListener() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        ActivityBase.activity.registerReceiver(smsVerificationReceiver, intentFilter)

        val mClient = SmsRetriever.getClient(ActivityBase.activity)
        val mTask = mClient.startSmsUserConsent(null)
        mTask.addOnSuccessListener {

        }
        mTask.addOnFailureListener {
            Toast.makeText(ActivityBase.activity, "Error", Toast.LENGTH_LONG).show()
        }

    }

    private fun setUiObserver() {
        accountViewModel.codeSent.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->

            }
        })

        accountViewModel.passwordResetSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                showInfoDialog("Success", "Password Changed Successfully", this)
            }
        })

        accountViewModel.passwordResetFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
            }
        })

        accountViewModel.codeSentFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
            }
        })
    }

    private fun setListeners() {
        binding.rlBack.setOnClickListener(this)
        binding.tvResend.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)
        binding.ivPass.setOnClickListener(this)
        binding.ivConfirmPass.setOnClickListener(this)
        binding.etPass.addTextChangedListener(this)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer((60 * 20 * 1000).toLong(), 100) {
            override fun onTick(l: Long) {
                val seconds = l / 1000
                val currentMinute = seconds / 60
                binding.tvTimer.text = String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60)
                if (currentMinute == 0L && seconds == 0L) {
                    binding.tvResend.visibility = View.VISIBLE
                    binding.tvTimer.text = "00:00"
                    if (countDownTimer != null) {
                        countDownTimer!!.cancel()
                    }
                } else {

                }
            }

            override fun onFinish() {
                binding.tvResend.visibility = View.VISIBLE
                binding.tvTimer.text = "00:00"
                if (countDownTimer != null) {
                    countDownTimer!!.cancel()
                }
            }
        }.start()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvResend -> {
                if (countDownTimer != null)
                    countDownTimer!!.cancel()
                startTimer()
                accountViewModel.sendForgotVerificationCode(SendVerificationCodeRequestModel(userName, "", ""))
                binding.tvResend.visibility = View.GONE
            }
            R.id.rlBack -> {
                if (countDownTimer != null)
                    countDownTimer!!.cancel()
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }
            R.id.btnDone -> {
                if (validateInput()) {
                    accountViewModel.forgotPassword(ForgotPasswordRequestModel(userName, binding.etCode.text.toString(), "", binding.etPass.text.toString()))
                }
            }
            R.id.ivPass -> {
                if (!TextUtils.isEmpty(binding.etPass.text)) {
                    isShow = !isShow
                    if (isShow) {
                        binding.ivPass.setImageResource(R.drawable.ic_hide_pass)
                        binding.etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.etPass.setSelection(binding.etPass.text!!.length)
                    } else {
                        binding.ivPass.setImageResource(R.drawable.ic_show_pass)
                        binding.etPass.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.etPass.setSelection(binding.etPass.text!!.length)
                    }
                }
            }
            R.id.ivConfirmPass -> {
                if (!TextUtils.isEmpty(binding.etConfirmPass.text)) {
                    isConfirmShow = !isConfirmShow
                    if (isConfirmShow) {
                        binding.ivConfirmPass.setImageResource(R.drawable.ic_hide_pass)
                        binding.etConfirmPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.etConfirmPass.setSelection(binding.etConfirmPass.text!!.length)
                    } else {
                        binding.ivConfirmPass.setImageResource(R.drawable.ic_show_pass)
                        binding.etConfirmPass.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.etConfirmPass.setSelection(binding.etConfirmPass.text!!.length)
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etCode.text.toString())) {
            binding.etCode.requestFocus()
            binding.etCode.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etPass.text.toString())) {
            binding.etPass.requestFocus()
            binding.etPass.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etConfirmPass.text.toString())) {
            binding.etConfirmPass.requestFocus()
            binding.etConfirmPass.errorAnim(ActivityBase.activity)
            return false
        } else if (!isLength || !isUp || !isLower || !isSpecial || !isNum) {
            binding.etPass.requestFocus()
            binding.etPass.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etConfirmPass.text.toString())) {
            binding.etConfirmPass.requestFocus()
            binding.etConfirmPass.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etConfirmPass.text.toString() != binding.etPass.text.toString()) {
            binding.etConfirmPass.requestFocus()
            ActivityBase.activity.showToastMessage("Password doesn't match")
            binding.etConfirmPass.errorAnim(ActivityBase.activity)
            return false
        } else
            return true
    }

    private fun checkPassword() {
        if (!TextUtils.isEmpty(binding.etPass.text.toString())) {
            val password = binding.etPass.text.toString()
            val hasUppercase = password != password.toLowerCase()
            val hasLowercase = password != password.toUpperCase()
            if (hasUppercase) {
                isUp = true
                binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isUp = false
                binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }

            if (hasLowercase) {
                isLower = true
                binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isLower = false
                binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }

            if (password.length > 8) {
                isLength = true
                binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isLength = false
                binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }

            val mPattern: Pattern = Pattern.compile("[/`©™®˜‡¬¿円÷¥€π.,;:!@#$%&*()_+=|<>?{}\\[\\]~-]")

            val hasSpecial: Matcher = mPattern.matcher(password)
            if (hasSpecial.find()) {
                isSpecial = true
                binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isSpecial = false
                binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }

            val mPatternNum: Pattern = Pattern.compile("[0-9]")

            val hasDigit: Matcher = mPatternNum.matcher(password)
            if (hasDigit.find()) {
                isNum = true
                binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isNum = false
                binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
                binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            }

        } else {
            binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
            binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        checkPassword()
    }

    override fun onClickOk() {
        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)!!
                    val oneTimeCode = parseOneTimeCode(message)
                    if (!TextUtils.isEmpty(oneTimeCode)){
                        binding.etCode.setText(oneTimeCode)
                    }
                }
        }

    }

    private fun parseOneTimeCode(message: String?): String {
        var myCode = ""
        if(message!!.contains("Movo")){
            try {
                val p = Pattern.compile("[0-9]{4}");
                val m = p.matcher(message);
                if (m.find()){
                    myCode = m.group()
                }
            } catch (e: Exception) {
            }
        }
        return myCode
    }
}