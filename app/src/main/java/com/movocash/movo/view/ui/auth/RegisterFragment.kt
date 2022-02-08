package com.movocash.movo.view.ui.auth

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.SignUpPersonalInfoRequestModel
import com.movocash.movo.data.model.responsemodel.IDNameResponseModel
import com.movocash.movo.databinding.FragmentRegisterBinding
import com.movocash.movo.kycFlow.Failed_Signup
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.Constants.FAIL
import com.movocash.movo.utilities.Constants.ID_VERFICATION
import com.movocash.movo.utilities.Constants.INSTANT_PASS
import com.movocash.movo.utilities.Constants.OTP_SCREEN
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetDatePickerFragment
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.main.profile.WebContentFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.util.*


class RegisterFragment : BaseFragment(), View.OnClickListener, BottomSheetDatePickerFragment.ISelectDobListener, BottomSheetSelectorFragment.ISelectListener, IInfoListener {

    lateinit var binding: FragmentRegisterBinding
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var accountViewModel: AccountViewModel
    private var finalDob: String? = null
    private var conversationId: String? = null
    private var myCountryList: ArrayList<String> = ArrayList()
    private var countryPosition = 0
    private var myStateList: ArrayList<String> = ArrayList()
    private var statePosition = 0
    private var typeList: ArrayList<String> = ArrayList()
    private var typePosition = 0
    private var countryList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private var stateList: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private var typeSelection: ArrayList<IDNameResponseModel.Model> = ArrayList()
    private var isCountrySet = false
    private var isStateSet = false
    private var isTypeSet = false
    private var isNumberValid = false
    private var isEmailValid = false



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setListener()
        initView()
        getStates()
        getSelectionType()
        maskSSN()
        //showStylishDialog("You've  Just Downloaded a Powerful Fintech App with Security that Matters.",getString(R.string.msg1),getText(R.string.msg2).toString())

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
        }

        // The callback can be enabled or disabled here or in the lambda

        return binding.root
    }



    private fun setInputFilters() {
        binding.etFirstName.inputType = (InputType.TYPE_TEXT_FLAG_CAP_WORDS) and (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        binding.etLastName.inputType = (InputType.TYPE_TEXT_FLAG_CAP_WORDS) and (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        binding.etEmail.inputType = (InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS) and (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        binding.etAddress1.inputType = (InputType.TYPE_TEXT_FLAG_CAP_WORDS) and (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        binding.etCity.inputType = (InputType.TYPE_TEXT_FLAG_CAP_WORDS) and (InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
    }

    private fun maskSSN() {
        binding.etSecurityNumber.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.etSecurityNumber.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.etSecurityNumber.setSelection(binding.etSecurityNumber.text!!.length)
            } else {
                binding.etSecurityNumber.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.etSecurityNumber.setSelection(binding.etSecurityNumber.text!!.length)
            }
        }
    }

    private fun initView() {
        MovoApp.initThreatMatrix()
        /*binding.ccpNumber.setDefaultCountryUsingNameCode("us")
        binding.ccpNumber.resetToDefaultCountry()
        binding.ccpNumber.registerPhoneNumberTextView(binding.etPhone)*/
        binding.etPhone.hint=""
        /*binding.ccpNumber.setPhoneNumberInputValidityListener { _, b ->
            isNumberValid = b
        }*/
    }


    private fun getSelectionType() {
        binding.isTypeLoading = true
        commonViewModel.getIdentificationTypes()
    }

    private fun getStates() {
        binding.isStateLoading = true
        commonViewModel.getStateByCountry(233)
    }

    private fun getCountries() {
        binding.isCountryLoading = true
        commonViewModel.getCountries()
    }

    private fun setUiObserver() {
        commonViewModel.countriesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.isCountryLoading = false
                if (obj.list != null && obj.list!!.size > 0) {
                    if (countryList.size > 0)
                        countryList.clear()
                    countryList.addAll(obj.list!!)

                    binding.tvCountry.text = "United States"
                    countryPosition = 0
                    isCountrySet = true
                    binding.isStateLoading = true
                    commonViewModel.getStateByCountry(233)
                }
            }


        })

        commonViewModel.identificationResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.isTypeLoading = false
                if (obj.list != null && obj.list!!.size > 0) {
                    if (typeSelection.size > 0)
                        typeSelection.clear()
                    typeSelection.addAll(obj.list!!)

                    if (typeSelection.size == 1) {
                        typePosition = 0
                        binding.tvSelectionType.text = typeSelection[0].name
                        isTypeSet = true
                    }
                }
            }
        })

        commonViewModel.statesResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                isCountrySet = true
                binding.isStateLoading = false
                if (obj.list != null && obj.list!!.size > 0) {
                    if (stateList.size > 0)
                        stateList.clear()
                    stateList.addAll(obj.list!!)
                    /*ActivityBase.activity.showToastMessage("UI oberserver State: "+"/"+stateList[0].id+"/"+stateList[1].id+"/"+stateList[2].id+"/"+stateList[3].id+"/"+stateList[4].id+"/"+stateList[5].id
                            +stateList[6].id+"/"+stateList[7].id+"/"+stateList[8].id+"/"+stateList[9].id+"/"+stateList[10].id+"/"+stateList[12].id)*/

                }
            }
        })

        accountViewModel.verificationResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->

                conversationId = obj.data
                MovoApp.db.putString(Constants.CONVERSATION_ID, conversationId!!)
                EnrollmentFragment.binding.vpEnrol.setCurrentItem(2, true)

            }
        })

        accountViewModel.verificationFailure.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->

                // showInfoDialog("",obj, this)
                ActivityBase.activity.showToastMessage(obj)

            }
        })

        accountViewModel.signupPersonalInfoFailure.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data!!.decisionId == INSTANT_PASS) {
                    callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
                } else if (obj.data!!.decisionId == FAIL) {
                    //ActivityBase.activity.showToastMessage("Instant Failed")

                    // MovoApp.db.putString(Constants.ERROR_CODE, obj.data!!.description!!)
                    //showInfoDialog("", obj.messages.toString(), this)
                    //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                    /*callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description), null)
                    (R.id.authContainer, SendCodeFragment.newInstance(true), null)*/
                    callFragmentWithReplace(R.id.authContainer, Failed_Signup.newInstance(obj.data!!.description), null)
                } else if (obj.data!!.decisionId == OTP_SCREEN) {
                    accountViewModel.sendVerificationCode(true)
                    // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                } else if (obj.data!!.decisionId == ID_VERFICATION) {
                    EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
            }
        })

        /* accountViewModel.verificationResponseModel.observe(viewLifecycleOwner, Observer {
             it?.let { msg ->
                 ActivityBase.activity.showToastMessage("success Case")

                 if(msg.data!!.decisionId == 1)
                 {
                     showInfoDialog("Congratulations", "You have successful registered your account. Please sign in to start MOVOing.", this)
                 }
                 else if(msg.data!!.decisionId == 2)
                 {

                     showInfoDialog("", msg.messages.toString(), this)
                     callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                 }
                 else if(msg.data!!.decisionId == 3)
                 {
                     accountViewModel.sendVerificationCode(true)
                     // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                 }
                 else if(msg.data!!.decisionId == 4)
                 {
                     EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                 }
             }
         })*/
        accountViewModel.verificationFailure.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                ActivityBase.activity.showToastMessage(msg)
                EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)

            }
        })
        accountViewModel.signupPersonalInfo.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                /*Constants.CONST_CC = "+${binding.ccpNumber.phoneNumber.countryCode}"
                Constants.CONST_NUM = binding.ccpNumber.phoneNumber.nationalNumber.toString()
                Constants.CONST_EMAIL = binding.etEmail.text.toString()*/
                //ActivityBase.activity.showToastMessage("success Case")

                if (obj.data!!.decisionId == INSTANT_PASS) {
                    callFragmentWithReplace(R.id.authContainer, SuccessFragment(), "SignInFragment")
                } else if (obj.data!!.decisionId == FAIL) {
                    //ActivityBase.activity.showToastMessage("Instant Failed")
                    //showInfoDialog("", obj.messages.toString(), this)
                    //callFragmentWithReplace(R.id.mainContainer, SignupFragment(), null)
                    callFragmentWithReplace(
                        R.id.authContainer,
                        Failed_Signup.newInstance(obj.data!!.description),
                        null
                    )
                } else if (obj.data!!.decisionId == OTP_SCREEN) {
                    accountViewModel.sendVerificationCode(true)
                    // EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                } else if (obj.data!!.decisionId == ID_VERFICATION) {
                    EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
                }
            }
        })

        /*accountViewModel.emailValid.observe(viewLifecycleOwner, Observer { isAvailable ->
            if (isAvailable) {
                isEmailValid = true
                binding.isUserLoading = false
                binding.tvEmailStatus.text = "Email Available"
                binding.tvEmailStatus.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isEmailValid = false
                binding.elEmail.expand()
                binding.isUserLoading = false
                binding.tvEmailStatus.text = "Email Already Exist"
                binding.tvEmailStatus.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.yellow))
            }


        })*/
    }

    private fun checkEmailExists() {
        binding.isUserLoading = true
       /* binding.elEmail.expand()
        binding.tvEmailStatus.text = "Checking Email..."
        binding.tvEmailStatus.setTextColor(
            ContextCompat.getColor(
                ActivityBase.activity,
                R.color.white
            )
        )*/
        accountViewModel.checkEmail(binding.etEmail.text.toString())
    }

    private fun verifyEmail() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                /*if (TextUtils.isEmpty(binding.etEmail.text.toString()))
                    binding.elEmail.collapse()
                Handler().postDelayed({
                    if (!TextUtils.isEmpty(binding.etEmail.text.toString())) {
                        if (ActivityBase.activity.isEmailValid(binding.etEmail.text.toString())) {
                            checkEmailExists()
                        } else {
                            binding.elEmail.collapse()
                        }
                    }
                }, 3000)*/
            }
        })
    }

    private fun setListener() {
        binding.tvCoastal.setOnClickListener(this)
        binding.tvCoastalPrivacy.setOnClickListener(this)
        binding.tvTerms.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
        binding.rlDate.setOnClickListener(this)
        binding.tvCancel.setOnClickListener(this)
//        binding.tvCountry.setOnClickListener(this)
//        binding.rlCountry.setOnClickListener(this)
        /////
        binding.rlState.setOnClickListener(this)
        binding.tvState.setOnClickListener(this)
//        binding.rlSelectionType.setOnClickListener(this)
//        binding.tvSelectionType.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvTerms -> addFragment(
                R.id.authContainer,
                WebContentFragment.newInstance(Constants.WEB_AGREEMENT),
                "WebContentFragment"
            )
            R.id.tvCoastal -> addFragment(
                R.id.authContainer, WebContentFragment.newInstance(
                    Constants.WEB_COASTAL
                ), "WebContentFragment"
            )
            R.id.tvCoastalPrivacy -> addFragment(
                R.id.authContainer, WebContentFragment.newInstance(
                    Constants.WEB_MOVO_PRIVACY
                ), "WebContentFragment"
            )
            R.id.btnContinue -> {
                if (validateInput()) {
                    /**Track Event when user clicks on continue button*/
                    val paramBundle = Bundle()
                    paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                    FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                        requireContext(),
                        CustomEventNames.EVENT_CLICK_PROFILE_CONT,
                        paramBundle
                    )

                    signUpTheUser()
                }
                // signUpTheUser()
            }
            R.id.rlDate -> showDateDialog()
            R.id.tvCancel -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.tvCountry -> {
                if (binding.isCountryLoading != null && !binding.isCountryLoading!! && countryList.size != 0) {
                    showCountryDialog()
                }
            }
            R.id.rlCountry -> {
                if (binding.isCountryLoading != null && !binding.isCountryLoading!! && countryList.size != 0) {
                    showCountryDialog()
                }
            }
            R.id.tvState -> {

                if (binding.isStateLoading != null && !binding.isStateLoading!! && stateList.size != 0) {
                    showStateDialog()
                } else
                    ActivityBase.activity.showToastMessage("Please Select Country First")

                /* if (binding.tvState.text.toString().equals("Select State"))
                    {
                       binding.tvState.setText("State*")
                     }*/


            }
            R.id.rlState -> {
                if (binding.isStateLoading != null && !binding.isStateLoading!! && stateList.size != 0) {
                    showStateDialog()
                } else
                    ActivityBase.activity.showToastMessage("Please Select Country First")
            }
            R.id.tvSelectionType -> {
                if (binding.isTypeLoading != null && !binding.isTypeLoading!!) {
                    showTypeSelectionDialog()
                }
            }
            R.id.rlSelectionType -> {
                if (binding.isTypeLoading != null && !binding.isTypeLoading!!) {
                    showTypeSelectionDialog()
                }
            }
        }
    }

    private fun signUpTheUser() {
        val genderId = if (binding.cbMale.isChecked) 1 else 2
        /*ActivityBase.activity.showToastMessage("UI oberserver State: "+"/"+stateList[0].id+"/"+stateList[1].id+"/"+stateList[2].id+"/"+stateList[3].id+"/"+stateList[4].id+"/"+stateList[5].id
                +stateList[6].id+"/"+stateList[7].id+"/"+stateList[8].id+"/"+stateList[9].id+"/"+stateList[10].id+"/"+stateList[12].id)
       // ActivityBase.activity.showToastMessage("State: "+stateList[statePosition].id+"/"+"position: "+statePosition)
        ActivityBase.activity.showToastMessage("value passing to API: "+stateList[statePosition].id)*/
        accountViewModel.signupPersonalInfo(
            SignUpPersonalInfoRequestModel(
                binding.etFirstName.text.toString(),
                binding.etMiddleName.text.toString(),
                binding.etLastName.text.toString(),
                typeSelection[typePosition].id,
                binding.etSecurityNumber.text.toString(),
                finalDob!!,
                genderId,
                "+1",
                binding.etPhone.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etAddress1.text.toString(),
                233,
                stateList[statePosition].id,
                binding.etCity.text.toString(),
                binding.etZip.text.toString(),
                MovoApp.db.getString(
                    Constants.MY_USER_ID
                )!!,
                MovoApp.db.getString(Constants.SESSION_ID)!!,
                MovoApp.db.getString(Constants.SESSION_ID_STATUS)!!

            )

        )


    }

    private fun validateInput(): Boolean {
        if (binding.etZip.text.toString().length < 5) {
            binding.svMain.scrollTo(0, 0)
            binding.etZip.requestFocus()
            binding.etZip.errorAnim(ActivityBase.activity)
            return false
        }
        if (TextUtils.isEmpty(binding.etFirstName.text.toString())) {
            binding.svMain.scrollTo(0, 0)
            binding.etFirstName.requestFocus()
            binding.etFirstName.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etLastName.text.toString())) {
            binding.svMain.scrollTo(0, 0)
            binding.etLastName.requestFocus()
            binding.etLastName.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.tvSelectionType.text.toString())) {
            binding.svMain.scrollTo(0, 0)
            ActivityBase.activity.showToastMessage("Please Select Identification")
            return false
        } else if (TextUtils.isEmpty(binding.etSecurityNumber.text.toString())) {
            binding.svMain.scrollTo(0, 0)
            binding.etSecurityNumber.requestFocus()
            binding.etSecurityNumber.errorAnim(ActivityBase.activity)
            return false
        }

        else if (binding.etSecurityNumber.text.toString().length < 9) {
            binding.svMain.scrollTo(0, 0)
            binding.etSecurityNumber.requestFocus()
            binding.etSecurityNumber.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid SSN")
            return false
        } else if (!TextUtils.isDigitsOnly(binding.etSecurityNumber.text.toString())) {
            binding.svMain.scrollTo(0, 0)
            binding.etSecurityNumber.requestFocus()
            binding.etSecurityNumber.setText("")
            binding.etSecurityNumber.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid SSN")
            return false
        } else if (TextUtils.isEmpty(binding.tvDOB.text.toString())) {
            ActivityBase.activity.showToastMessage("Please Select Date Of Birth")
            return false
        } /*else if (TextUtils.isEmpty(binding.etEmail.text.toString())) {
            binding.etEmail.requestFocus()
            binding.etEmail.errorAnim(ActivityBase.activity)
            return false
        }*/ /*else if (!ActivityBase.activity.isEmailValid(binding.etEmail.text.toString())) {
            binding.etEmail.requestFocus()
            binding.etEmail.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid Email")
            return false
        }*/ else if (TextUtils.isEmpty(binding.etPhone.text.toString())) {
            binding.etPhone.requestFocus()
            binding.etPhone.errorAnim(ActivityBase.activity)
            return false
        } /*else if (!binding.ccpNumber.isValid) {
            binding.etPhone.requestFocus()
            binding.etPhone.errorAnim(ActivityBase.activity)
            return false
        }*/ else if (TextUtils.isEmpty(binding.etAddress1.text.toString())) {
            binding.etAddress1.requestFocus()
            binding.etAddress1.errorAnim(ActivityBase.activity)
            return false
        } else if (!isCountrySet) {
            ActivityBase.activity.showToastMessage("Please Select Country")
            return false
        } else if (!isCountrySet) {
            ActivityBase.activity.showToastMessage("Please Select City")
            return false
        } else if (TextUtils.isEmpty(binding.etCity.text.toString())) {
            binding.etCity.requestFocus()
            binding.etCity.errorAnim(ActivityBase.activity)
            return false
        } else if (!binding.cbAgree.isChecked) {
            ActivityBase.activity.showToastMessage("Please Agree Terms & Conditions")
            return false
        } else if (!binding.cbAgreeCoastal.isChecked) {
            ActivityBase.activity.showToastMessage("Please Agree Coastal Terms & Conditions")
            return false
        } else if (binding.tvState.text.toString().equals("State*")) {
            ActivityBase.activity.showToastMessage("Please Select State")
            return false
        }else if (TextUtils.isEmpty(binding.tvState.text.toString())) {
            binding.tvState.requestFocus()
            binding.tvState.errorAnim(ActivityBase.activity)
            return false
        }else {
            return true
        }
    }

    private fun showTypeSelectionDialog() {
        if (typeList.size > 0)
            typeList.clear()

        typeSelection.mapTo(typeList) { it.name }
        setBottomSheet(typeList, typePosition, 11)
    }

    private fun showStateDialog() {
        if (myStateList.size > 0)
            myStateList.clear()
        myStateList.add("Select State")

        stateList.mapTo(myStateList) { it.name }
        setBottomSheet(myStateList, statePosition, 10)


    }

    private fun showCountryDialog() {
        if (myCountryList.size > 0)
            myCountryList.clear()

        countryList.mapTo(myCountryList) { it.name }
        setBottomSheet(myCountryList, countryPosition, 9)
    }

    private fun setBottomSheet(list: ArrayList<String>, selectedPosition: Int, type: Int) {
        val bottomSheet = BottomSheetSelectorFragment(list, selectedPosition, type)
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)


        bottomSheet.setMyListener(this)


        /* if (binding.tvState.text.toString().equals("Select State"))
         {
             binding.tvState.setText("State*")
         }*/

    }

    private fun showDateDialog() {
        val bottomSheet: BottomSheetDatePickerFragment = if (finalDob != null) {
            BottomSheetDatePickerFragment(finalDob!!, Constants.CONST_DOB)
        } else {
            BottomSheetDatePickerFragment("", Constants.CONST_DOB)
        }
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onSelectDob(dob: String) {
        finalDob = dob
        val sp2 = finalDob!!.split("-")
        val year = sp2[0]
        val month = (sp2[1]).toInt()
        val day = sp2[2]
        if (month < 10)
            binding.tvDOB.text = "0$month-$day-$year"
        else
            binding.tvDOB.text = "$month-$day-$year"
    }

    override fun onSelectDob(dob: String, view: View) {
        TODO("Not yet implemented")
    }

    override fun onDateCancel() {
    }

    override fun onSelect(pos: Int, type: Int) {
        when (type) {
            9 -> {
                countryPosition = pos - 1
                binding.tvCountry.text = myCountryList[pos]
                binding.isStateLoading = true
                commonViewModel.getStateByCountry(countryList[countryPosition].id)
                binding.tvState.text = ""
                isStateSet = false
                isCountrySet = true
                statePosition = 0
//                checkAndChange()
            }
            10 -> {
                if (pos == 1) {
                    binding.tvState.text = "State*"
                } else {
                    statePosition = pos - 2
                    binding.tvState.text = myStateList[pos]
                    // ActivityBase.activity.showToastMessage("State selected: "+myStateList[statePosition]+"/"+"position: "+statePosition)
                    isStateSet = true
                }
//                checkAndChange()
            }
            11 -> {
                typePosition = pos - 1
                binding.tvSelectionType.text = typeList[pos]
                isTypeSet = true
//                checkAndChange()
            }
        }
    }

    override fun onCancel(type: Int) {

    }

/*    override fun onClickOk() {
        Constants.CONST_CC = "+${binding.ccpNumber.phoneNumber.countryCode}"
        Constants.CONST_NUM = binding.ccpNumber.phoneNumber.nationalNumber.toString()
        Constants.CONST_EMAIL = binding.etEmail.text.toString()
        EnrollmentFragment.binding.vpEnrol.setCurrentItem(1, true)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }*/

    override fun onClickOk() {
        callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
    }

    override fun onClickCancel() {
        callFragmentWithReplace(R.id.authContainer, SignInFragment(), null)
    }


}