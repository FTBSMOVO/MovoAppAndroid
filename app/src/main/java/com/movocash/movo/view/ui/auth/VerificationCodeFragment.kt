package com.movocash.movo.view.ui.auth

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
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
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.VerifyCodeRequestModel
import com.movocash.movo.data.model.requestmodel.VerifySignupCodeRequestModel
import com.movocash.movo.data.model.responsemodel.IDNameResponseModel
import com.movocash.movo.databinding.FragmentVerificationCodeBinding
import com.movocash.movo.kycFlow.Failed_Signup
import com.movocash.movo.kycFlow.SignupFragment
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern


class VerificationCodeFragment : BaseFragment(), View.OnClickListener, TextWatcher, BottomSheetSelectorFragment.ISelectListener, View.OnKeyListener, IInfoListener {

    private var conversationId = ""
    private var isQuestionSet = false
    private var isLength = false
    private var isUp = false
    private var isLower = false
    private var isSpecial = false
    private var isNum = false
    lateinit var binding: FragmentVerificationCodeBinding
    private var questionList: ArrayList<String> = ArrayList()
    private var questionPosition = 0
    private var mQuestionList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var accountViewModel: AccountViewModel
    private var isUserNameValid = false
    private var pin = ""
    private var isShow = false
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


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            //accountViewModel.sendVerificationCode(false)
            getSecretQuestions()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_code, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setView()
        setListener()
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

    private fun setView() {
       /* binding.tvEmail.text = Constants.CONST_EMAIL
        binding.tvNumber.text = "${Constants.CONST_CC}-${Constants.CONST_NUM}"*/
    }

    private fun verifyUserName() {
        /*binding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (TextUtils.isEmpty(binding.etUserName.text.toString()))
                    binding.elUserName.collapse()
                Handler().postDelayed({
                    if (!TextUtils.isEmpty(binding.etUserName.text.toString()))
                        checkUserName()
                }, 3000)
            }
        })*/
    }

    private fun checkUserName() {
       /* binding.isUserLoading = true
        binding.elUserName.expand()
        binding.tvUserNameStatus.text = "Checking Username..."
        binding.tvUserNameStatus.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.black))
        accountViewModel.checkUserName(binding.etUserName.text.toString())*/
    }


    private fun setUiObserver() {


       /* accountViewModel.verificationSuccess.observe(viewLifecycleOwner, Observer {
            setThings()
            showInfoDialog("Congratulations", "You have successful registered your account. Please sign in to start MOVOing.", this)


        })*/

      /*  accountViewModel.verifyFailure.observe(viewLifecycleOwner, Observer { msg ->
            showConfirmationDialog(msg,object : IInfoListener {
                override fun onClickOk() {
                    callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), null)
                }
                override fun onClickCancel() {
                    //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)

                    EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
                }
            })
        })*/

        accountViewModel.verificationResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj != null) {
                    conversationId = obj.data!!
                }
            }
        })


        accountViewModel.signUpCodeFailure.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                //ActivityBase.activity.showToastMessage(obj.messages.toString())
                if(obj.data!!.decisionId == Constants.INSTANT_PASS)
                {
                    callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
                }
                else if(obj.data!!.decisionId == Constants.FAIL)
                {
                    //ActivityBase.activity.showToastMessage("Instant Failed")
                    //showInfoDialog("", obj.messages.toString(), this)
                    //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                    callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description), null)
                }
                else if(obj.data!!.decisionId == Constants.OTP_SCREEN)
                {
                    accountViewModel.sendVerificationCode(true)
                    // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
                else if(obj.data!!.decisionId == Constants.ID_VERFICATION)
                {
                    EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
            }
        })

        accountViewModel.signUpCodeSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
               // ActivityBase.activity.showToastMessage("success Case")

                if(obj.data!!.decisionId == Constants.INSTANT_PASS)
                {
                    callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
                }
                else if(obj.data!!.decisionId == Constants.FAIL)
                {
                    //ActivityBase.activity.showToastMessage("Instant Failed")
                    //showInfoDialog("", obj.messages.toString(), this)
                    //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                    callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description.toString()), null)
                }
                else if(obj.data!!.decisionId == Constants.OTP_SCREEN)
                {
                    accountViewModel.sendVerificationCode(true)
                    // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
                else if(obj.data!!.decisionId == Constants.ID_VERFICATION)
                {
                    EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
            }
        })



        accountViewModel.userNameInValid.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun setThings() {
        /*if (binding.cbAgree.isChecked) {
            MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, binding.etUserName.text.toString())
        }*/
    }

    private fun getSecretQuestions() {
        /*binding.isQuestionLoading = true
        commonViewModel.getQuestions()*/
    }

    private fun setListener() {
        /*
        binding.tvResendCode.setOnClickListener(this)

        binding.ivPass.setOnClickListener(this)
        binding.tvSecretQuestion.setOnClickListener(this)
        binding.etPass.addTextChangedListener(this)*/
        binding.tvCancel.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.et1.addTextChangedListener(this)
        binding.et2.addTextChangedListener(this)
        binding.et3.addTextChangedListener(this)
        binding.et4.addTextChangedListener(this)

        binding.et1.setOnKeyListener(this)
        binding.et2.setOnKeyListener(this)
        binding.et3.setOnKeyListener(this)
        binding.et4.setOnKeyListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvCancel -> {
                //EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
            }
            R.id.btnSubmit -> {
                /*if (validateInput()) {
                   // pin = "${binding.et1.text.toString()}${binding.et2.text.toString()}${binding.et3.text.toString()}${binding.et4.text.toString()}${binding.et5.text.toString()}${binding.et6.text.toString()}"
                   // accountViewModel.verifyVerificationCode(VerifyCodeRequestModel(binding.etUserName.text.toString(), binding.etPass.text.toString(), mQuestionList[questionPosition].id, binding.etAnswer.text.toString(), pin, MovoApp.db.getString(Constants.SESSION_ID)!!, conversationId))
                }*/
                pin = "${binding.et1.text.toString()}${binding.et2.text.toString()}${binding.et3.text.toString()}${binding.et4.text.toString()}"
                accountViewModel.verifySignupVerificationCode(VerifySignupCodeRequestModel(pin,MovoApp.db.getString(Constants.MY_USER_ID)!!,conversationId))

            }

            R.id.tvResendCode -> accountViewModel.sendVerificationCode(true)

        }
    }

    private fun showSecretDialog() {
        if (questionList.size > 0)
            questionList.clear()

        mQuestionList.mapTo(questionList) { it.name }
        setBottomSheet(questionList, questionPosition, 1)
    }

    private fun setBottomSheet(list: ArrayList<String>, selectedPosition: Int, type: Int) {
        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, type)
        bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {


        if (binding.et1.hasFocus()) {
            if (binding.et1.text.toString().length == 1) {
                binding.et1.clearFocus()
                binding.et2.requestFocus()
            }
        } else if (binding.et2.hasFocus()) {
            if (binding.et2.text.toString().length == 1) {
                binding.et2.clearFocus()
                binding.et3.requestFocus()
            }

        } else if (binding.et3.hasFocus()) {
            if (binding.et3.text.toString().length == 1) {
                binding.et3.clearFocus()
                binding.et4.requestFocus()
            }

        } else if (binding.et4.hasFocus()) {
            if (binding.et4.text.toString().length == 1) {

            }
        }

    }

   /* private fun checkPassword() {
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
    }*/

    override fun onSelect(pos: Int, type: Int) {
        when (type) {
            1 -> {
                questionPosition = pos - 1
               // binding.tvSecretQuestion.text = questionList[pos]
                isQuestionSet = true
            }
        }
    }

    override fun onCancel(type: Int) {
    }

    private fun validateInput(): Boolean {

         if (TextUtils.isEmpty(binding.et1.text.toString())) {
            binding.et1.requestFocus()
            binding.et1.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.et2.text.toString())) {
            binding.et2.requestFocus()
            binding.et2.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.et3.text.toString())) {
            binding.et3.requestFocus()
            binding.et3.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.et4.text.toString())) {
            binding.et4.requestFocus()
            binding.et4.errorAnim(ActivityBase.activity)
            return false
        }
        else {
            return true
        }
    }

    override fun onKey(v: View?, keyCode: Int, p2: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL)
            when (v!!.id) {
                R.id.et1 -> {

                }
                R.id.et2 -> {
                    if (TextUtils.isEmpty(binding.et2.text.toString())) {
                        binding.et2.clearFocus()
                        binding.et1.requestFocus()
                    } else {
                        binding.et2.setText("")
                    }
                }
                R.id.et3 -> {
                    if (TextUtils.isEmpty(binding.et3.text.toString())) {
                        binding.et3.clearFocus()
                        binding.et2.requestFocus()
                    } else {
                        binding.et3.setText("")
                    }
                }
                R.id.et4 -> {
                    if (TextUtils.isEmpty(binding.et4.text.toString())) {
                        binding.et4.clearFocus()
                        binding.et3.requestFocus()
                    } else {
                        binding.et4.setText("")
                    }
                }

            }

        return false
    }

    override fun onClickOk() {
        callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
    }

    override fun onClickCancel() {
        callFragmentWithReplace(R.id.authContainer, SignupFragment(), null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)!!
                    val oneTimeCode = parseOneTimeCode(message)
                    if (!TextUtils.isEmpty(oneTimeCode)) {
                        binding.et1.setText(oneTimeCode[0].toString())
                        binding.et2.setText(oneTimeCode[1].toString())
                        binding.et3.setText(oneTimeCode[2].toString())
                        binding.et4.setText(oneTimeCode[3].toString())
                        /*binding.et5.setText(oneTimeCode[4].toString())
                        binding.et6.setText(oneTimeCode[5].toString())*/
                    }
                }
        }

    }

    private fun parseOneTimeCode(message: String?): String {
        var myCode = ""
        if (message!!.contains("Movo")) {
            try {
                val p = Pattern.compile("[0-9]{6}");
                val m = p.matcher(message);
                if (m.find()) {
                    myCode = m.group()
                }
            } catch (e: Exception) {
            }
        }
        return myCode
    }

}