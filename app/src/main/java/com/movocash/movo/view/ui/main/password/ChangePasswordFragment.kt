package com.movocash.movo.view.ui.main.password

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.repository.BiometricInfoRepository
import com.movocash.movo.databinding.FragmentChangePasswordBinding
import com.movocash.movo.persistence.tables.BiometricInfoModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class ChangePasswordFragment : BaseFragment(), View.OnClickListener, TextWatcher {

    lateinit var binding: FragmentChangePasswordBinding
    private lateinit var accountViewModel: AccountViewModel
    private val biometricInfoRepository = BiometricInfoRepository(ActivityBase.activity)
    private var isLength = false
    private var isUp = false
    private var isLower = false
    private var isSpecial = false
    private var isNum = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        binding.tvTitle.isSelected = true
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        accountViewModel.passwordChangeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        accountViewModel.passwordChangeSuccess.observe(viewLifecycleOwner, Observer {
            updateAndProceed()
        })
    }

    private fun updateAndProceed() {
        MovoApp.db.putString(Constants.USER_PASS, binding.etNewPass.text.toString())
        var obj: BiometricInfoModel? = null
        MovoApp.scope.launch(Dispatchers.IO) {
            obj = biometricInfoRepository.getAllBiometricData()
        }

        if (obj != null) {
            if (obj!!.email == MovoApp.db.getString(Constants.USER_NAME)) {
                biometricInfoRepository.updateBiometricData(MovoApp.db.getString(Constants.USER_NAME)!!, MovoApp.db.getString(Constants.USER_PASS)!!)
            }
        }

        ActivityBase.activity.showToastMessage("Password Updated")
        /*gotoHome()
        callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "MyCardsFragment")*/
        binding.etConfirm.setText("")
        binding.etNewPass.setText("")
        binding.etCurrentPass.setText("")
    }

    private fun setListeners() {
        binding.rlRight.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.etNewPass.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlRight -> {
                if (validateInput()) {
                    accountViewModel.changePassword(binding.etCurrentPass.text.toString(), binding.etNewPass.text.toString())
                }
            }
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
        }
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etCurrentPass.text.toString())) {
            binding.etCurrentPass.requestFocus()
            binding.etCurrentPass.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etNewPass.text.toString())) {
            binding.etNewPass.requestFocus()
            binding.etNewPass.errorAnim(ActivityBase.activity)
            return false
        } else if (!isLength || !isUp || !isLower || !isSpecial || !isNum) {
            binding.etNewPass.requestFocus()
            binding.etNewPass.errorAnim(ActivityBase.activity)
            return false
        } else if (TextUtils.isEmpty(binding.etConfirm.text.toString())) {
            binding.etConfirm.requestFocus()
            binding.etConfirm.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etConfirm.text.toString() != binding.etNewPass.text.toString()) {
            binding.etConfirm.requestFocus()
            ActivityBase.activity.showToastMessage("Password doesn't match")
            binding.etConfirm.errorAnim(ActivityBase.activity)
            return false
        } else
            return true
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        checkPassword()
    }

    private fun checkPassword() {
        if (!TextUtils.isEmpty(binding.etNewPass.text.toString())) {
            val password = binding.etNewPass.text.toString()
            val hasUppercase = password != password.toLowerCase()
            val hasLowercase = password != password.toUpperCase()
            if (hasUppercase) {
                isUp = true
                binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isUp = false
                binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

            if (hasLowercase) {
                isLower = true
                binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isLower = false
                binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

            if (password.length > 8) {
                isLength = true
                binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isLength = false
                binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

            val mPattern: Pattern = Pattern.compile("[/`©™®˜‡¬¿円÷¥€π.,;:!@#$%&*()_+=|<>?{}\\[\\]~-]")

            val hasSpecial: Matcher = mPattern.matcher(password)
            if (hasSpecial.find()) {
                isSpecial = true
                binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isSpecial = false
                binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

            val mPatternNum: Pattern = Pattern.compile("[0-9]")

            val hasDigit: Matcher = mPatternNum.matcher(password)
            if (hasDigit.find()) {
                isNum = true
                binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
                binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.green))
            } else {
                isNum = false
                binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
                binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            }

        } else {
            binding.tvUpperCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvUpperCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvLowerCase1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvLowerCase2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvNumber1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvNumber2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvLength1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvLength2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvSpecial1.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
            binding.tvSpecial2.setTextColor(ContextCompat.getColor(ActivityBase.activity, R.color.white))
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