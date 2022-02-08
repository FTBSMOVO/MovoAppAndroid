package com.movocash.movo.kycFlow

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.SignUpRequestModel2
import com.movocash.movo.data.model.requestmodel.VerifySignupEmailCodeRequestModel
import com.movocash.movo.data.model.responsemodel.IDNameResponseModel
import com.movocash.movo.databinding.EmailOtpBinding
import com.movocash.movo.databinding.FragmentSignupBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.auth.EnrollmentFragment
import com.movocash.movo.view.ui.auth.WelcomeFragment
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignupFragment  : BaseFragment(), View.OnClickListener, TextWatcher, BottomSheetSelectorFragment.ISelectListener, View.OnKeyListener,
    IInfoListener {

    private var conversationId = ""
    private var signupData = ""
    private var isQuestionSet = false
    private var isLength = false
    private var isUp = false
    private var isLower = false
    private var isSpecial = false
    private var isNum = false
    lateinit var binding: FragmentSignupBinding
    private var questionList: ArrayList<String> = ArrayList()
    private var questionPosition = 0
    private var mQuestionList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var accountViewModel: AccountViewModel
    private var isUserNameValid = false
    private var pin = ""
    private var isShow = false
    private val SMS_CONSENT_REQUEST = 2  // Set to an unused request code
    companion object {
       // lateinit var binding: FragmentSignupBinding
        lateinit var mActivity: AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        getSecretQuestions()
        setUiObserver()
        //setView()
        setListener()
        //showStylishDialog("You've  Just Downloaded a Powerful Fintech App with Security that Matters.",getString(R.string.msg1),getText(R.string.msg2).toString())
       // showStylishDialog2("",getString(R.string.msg3),"")
        return binding.root
    }
/*    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()

                // And when you want to go back based on your condition
                *//*if (yourCondition) {
                    this.isEnabled = false
                    requireActivity().onBackPressed()
                }*//*
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }*/

    private fun signUpTheUser() {
        //val genderId = if (binding.cbMale.isChecked) 1 else 2
        accountViewModel.signUp2(
            SignUpRequestModel2(
                binding.etEmail.text.toString(),
                binding.etUserName.text.toString(),
                binding.etPass.text.toString(),
                mQuestionList[questionPosition].id,
                binding.etAnswer.text.toString()
            )
        )
    }
    private fun setUiObserver() {

        accountViewModel.signUpResponseModel.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                signupData = res.data.toString()
                MovoApp.db.putString(Constants.MY_USER_ID, signupData)

                // showInfoDialog("yes", res.messages!!, this)
                //showEmailOtp("yes", res.messages!!, this)
                callFragmentWithReplace(
                    R.id.authContainer,
                    EnrollmentFragment(),
                    "EnrollmentFragment"
                )
            }
        })

        accountViewModel.verifySignupEmailCodeSuccess.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                callFragmentWithReplace(
                    R.id.authContainer,
                    EnrollmentFragment(),
                    "EnrollmentFragment"
                )
            }
        })

      /*  override fun onClickOk() {
            //callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), null)
            // callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), "EnrollmentFragment")
        }
        override fun onClickCancel() {
            //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)

            //EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
        }*/
        accountViewModel.verifySignupEmailCodeFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
                Toast.makeText(context, res, Toast.LENGTH_LONG).show()

                //showInfoDialog("yes", res, this)
                // showEmailOtp("yes", res.messages!!, this)
            }
        })

       /* accountViewModel.signUpFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()
               // showInfoDialog("yes", res, this)
                signupData = res
                //showInfoDialog("yes", res, this)

                showInfoDialog("sda",res,object : IInfoListener {
                    override fun onClickOk() {
                        //callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), null)
                        Toast.makeText(context,res,Toast.LENGTH_LONG).show()

                    }
                    override fun onClickCancel() {
                        //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)

                        // EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
                    }
                })
            }
        })*/

        accountViewModel.signUpFailure.observe(this, Observer { msg ->
            showInfoDialog("sad", msg, object : IInfoListener {
                override fun onClickOk() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    //callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), null)
                }

                override fun onClickCancel() {
                    //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)

                    // EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
                }
            })
        })

        commonViewModel.questionsResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.isQuestionLoading = false
                if (obj.list != null && obj.list!!.size > 0) {
                    if (mQuestionList.size > 0)
                        mQuestionList.clear()
                    mQuestionList.addAll(obj.list!!)
                }
            }
        })

        accountViewModel.verificationSuccess.observe(this, Observer {
            //setThings()
            showInfoDialog(
                "Congratulations",
                "You have successful registered your account. Please sign in to start MOVOing.",
                this
            )
        })

        accountViewModel.verifyFailure.observe(this, Observer { msg ->

            ActivityBase.activity.showToastMessage(msg)
            /*showConfirmationDialog(msg,object : IInfoListener {
                override fun onClickOk() {
                    //callFragmentWithReplace(R.id.authContainer, EnrollmentFragment(), null)
                }
                override fun onClickCancel() {
                    //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)

                   // EnrollmentFragment.binding.vpEnrol.setCurrentItem(0, true)
                }
            })*/
        })

        accountViewModel.verificationResponseModel.observe(this, Observer {
            it?.let { obj ->
                if (obj != null) {
                    conversationId = obj.data!!
                }
            }
        })

      /*  accountViewModel.userNameValid.observe(this, Observer { isAvailable ->
            if (isAvailable) {
                isUserNameValid = true
                binding.isUserLoading = false
                binding.tvUserNameStatus.text = "Username Available"
                binding.tvUserNameStatus.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isUserNameValid = false
                binding.elUserName.expand()
                binding.isUserLoading = false
                binding.tvUserNameStatus.text = "Username Already Exist"
                binding.tvUserNameStatus.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.yellow))
            }


        })

        accountViewModel.userNameInValid.observe(this, Observer {

        })*/
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun getSecretQuestions() {
        binding.isQuestionLoading = true
        commonViewModel.getQuestions()
    }

    private fun setListener() {
        binding.tvCancel.setOnClickListener(this)
        binding.tvResendCode.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.ivPass.setOnClickListener(this)
        binding.tvSecretQuestion.setOnClickListener(this)
        binding.etPass.addTextChangedListener(this)
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
                callFragmentWithReplace(R.id.authContainer, WelcomeFragment(), "SignInFragment")
            }
            R.id.btnSubmit -> {
                /* if (validateInput()) {
                    pin = "${binding.et1.text.toString()}${binding.et2.text.toString()}${binding.et3.text.toString()}${binding.et4.text.toString()}${binding.et5.text.toString()}${binding.et6.text.toString()}"
                    accountViewModel.verifyVerificationCode(
                        VerifyCodeRequestModel(binding.etUserName.text.toString(), binding.etPass.text.toString(), mQuestionList[questionPosition].id, binding.etAnswer.text.toString(), pin, MovoApp.db.getString(
                            Constants.SESSION_ID)!!, conversationId)
                    )
                }*/

                if (validateInput()) {
                    /**track event when user clicks on continue in the sign up flow*/
                    val paramBundle = Bundle()
                    paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                    FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                        requireContext(),
                        CustomEventNames.EVENT_CLICK_SIGN_UP_CONT,
                        paramBundle
                    )

                    signUpTheUser()
                }
            }
            R.id.tvSecretQuestion -> {
                if (!binding.isQuestionLoading!!)
                    showSecretDialog()
            }
            R.id.tvResendCode -> accountViewModel.sendVerificationCode(true)
            R.id.ivPass -> {
                if (!TextUtils.isEmpty(binding.etPass.text)) {
                    isShow = !isShow
                    if (isShow) {
                        binding.etPass.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        binding.etPass.setSelection(binding.etPass.text!!.length)
                    } else {
                        binding.etPass.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                        binding.etPass.setSelection(binding.etPass.text!!.length)
                    }
                }
            }
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
    override fun afterTextChanged(s: Editable?) {
        if (binding.etPass.hasFocus()) {
            checkPassword()
        }

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
                binding.et4.clearFocus()
                binding.et5.requestFocus()
            }
        } else if (binding.et5.hasFocus()) {
            if (binding.et5.text.toString().length == 1) {
                binding.et5.clearFocus()
                binding.et6.requestFocus()
            }
        } else if (binding.et6.hasFocus()) {
            if (binding.et6.text.toString().length == 1) {

            }
        }
    }

    private fun checkPassword() {
        if (!TextUtils.isEmpty(binding.etPass.text.toString())) {
            val password = binding.etPass.text.toString()
            val hasUppercase = password != password.toLowerCase()
            val hasLowercase = password != password.toUpperCase()
            if (hasUppercase) {
                isUp = true
                binding.tvUpperCase1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
                binding.tvUpperCase2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
            } else {
                isUp = false
                binding.tvUpperCase1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
                binding.tvUpperCase2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
            }

            if (hasLowercase) {
                isLower = true
                binding.tvLowerCase1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
                binding.tvLowerCase2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
            } else {
                isLower = false
                binding.tvLowerCase1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
                binding.tvLowerCase2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
            }

            if (password.length > 8) {
                isLength = true
                binding.tvLength1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
                binding.tvLength2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
            } else {
                isLength = false
                binding.tvLength1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
                binding.tvLength2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
            }

            val mPattern: Pattern = Pattern.compile("[/`©™®˜‡¬¿円÷¥€π.,;:!@#$%&*()_+=|<>?{}\\[\\]~-]")

            val hasSpecial: Matcher = mPattern.matcher(password)
            if (hasSpecial.find()) {
                isSpecial = true
                binding.tvSpecial1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
                binding.tvSpecial2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
            } else {
                isSpecial = false
                binding.tvSpecial1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
                binding.tvSpecial2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
            }

            val mPatternNum: Pattern = Pattern.compile("[0-9]")

            val hasDigit: Matcher = mPatternNum.matcher(password)
            if (hasDigit.find()) {
                isNum = true
                binding.tvNumber1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
                binding.tvNumber2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.green
                    )
                )
            } else {
                isNum = false
                binding.tvNumber1.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
                binding.tvNumber2.setTextColor(
                    ContextCompat.getColor(
                        ActivityBase.activity,
                        R.color.black
                    )
                )
            }

        } else {
            binding.tvUpperCase1.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvUpperCase2.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvLowerCase1.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvLowerCase2.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvNumber1.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvNumber2.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvLength1.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvLength2.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvSpecial1.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
            binding.tvSpecial2.setTextColor(
                ContextCompat.getColor(
                    ActivityBase.activity,
                    R.color.black
                )
            )
        }
    }

    override fun onSelect(pos: Int, type: Int) {
        when (type) {
            1 -> {
                questionPosition = pos - 1
                binding.tvSecretQuestion.text = questionList[pos]
                isQuestionSet = true
            }
        }
    }

    override fun onCancel(type: Int) {
    }

    private fun validateInput(): Boolean {

        if (TextUtils.isEmpty(binding.etUserName.text.toString())) {
            binding.etUserName.requestFocus()
            binding.etUserName.errorAnim(ActivityBase.activity)
            return false
        } else if (!isLength || !isUp || !isLower || !isSpecial || !isNum) {
            binding.etPass.requestFocus()
            binding.etPass.errorAnim(ActivityBase.activity)
            return false
        } else if (!isQuestionSet) {
            ActivityBase.activity.showToastMessage("Please Select A Question")
            return false
        } else if (TextUtils.isEmpty(binding.etAnswer.text.toString())) {
            binding.etAnswer.requestFocus()
            binding.etAnswer.errorAnim(ActivityBase.activity)
            return false
        }else if (!binding.cbConfirm.isChecked) {
            binding.cbConfirm.requestFocus()
            binding.cbConfirm.errorAnim(ActivityBase.activity)
            return false
        }

        /*else  if (!binding.etPass.text.toString().equals(binding.etConfirPass.toString()))
                    {
                        binding.etConfirPass.requestFocus()
                        binding.etConfirPass.errorAnim(ActivityBase.activity)
                        return false
                     }*/
        /* else if (TextUtils.isEmpty(binding.et1.text.toString())) {
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
        } else if (TextUtils.isEmpty(binding.et5.text.toString())) {
            binding.et5.requestFocus()
            binding.et5.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.et6.text.toString())) {
            binding.et6.requestFocus()
            binding.et6.errorAnim(ActivityBase.activity)
            return false
        }*/ else {
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
        //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
      /*  val binding: EmailOtpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ActivityBase.activity),
            R.layout.email_otp,
            null,
            false
        )
        pin = "${binding.et1.text.toString()}${binding.et2.text.toString()}${binding.et3.text.toString()}${binding.et4.text.toString()}"
        accountViewModel.verifySignupEmailCode(
            VerifySignupEmailCodeRequestModel(
                MovoApp.db.getString(
                    Constants.MY_EMAIL_CODE
                )!!, signupData
            )
        )*/

    }

    override fun onClickCancel() {
        //callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
    }

}