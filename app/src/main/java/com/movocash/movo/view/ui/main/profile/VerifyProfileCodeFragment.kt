package com.movocash.movo.view.ui.main.profile

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.UpdateProfileRequestModel
import com.movocash.movo.databinding.FragmentVerifyProfileCodeBinding
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.AccountViewModel
import java.util.regex.Pattern

class VerifyProfileCodeFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentVerifyProfileCodeBinding
    private lateinit var accountViewModel: AccountViewModel
    private var countDownTimer: CountDownTimer? = null
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
        lateinit var instance: VerifyProfileCodeFragment
        lateinit var model: UpdateProfileRequestModel

        fun newInstance(obj: UpdateProfileRequestModel): VerifyProfileCodeFragment {
            instance = VerifyProfileCodeFragment()
            model = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_profile_code, container, false)
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
        accountViewModel.verifyProfileCodeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.profileCodeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })
        accountViewModel.updateProfileFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.updateProfileFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.profileCodeSent.observe(viewLifecycleOwner, Observer {
            startTimer()
            binding.tvResend.visibility = View.GONE
        })

        accountViewModel.verifyProfileCodeSuccess.observe(viewLifecycleOwner, Observer {
            accountViewModel.updateProfile(model)
        })


        accountViewModel.updateProfileResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                    showInfoDialog("Success", obj.data!!.responseDesc!!, this)
                }
            }
        })
    }

    private fun setListeners() {
        binding.rlBack.setOnClickListener(this)
        binding.tvResend.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)
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
                accountViewModel.sendProfileCode()
            }
            R.id.rlBack -> {
                if (countDownTimer != null)
                    countDownTimer!!.cancel()
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            }
            R.id.btnDone -> {
                if (!TextUtils.isEmpty(binding.etCode.text.toString())) {
                    accountViewModel.verifyProfileCode(binding.etCode.text.toString())
                } else {
                    binding.etCode.requestFocus()
                    binding.etCode.errorAnim(ActivityBase.activity)
                }

            }
        }
    }

    override fun onClickOk() {
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