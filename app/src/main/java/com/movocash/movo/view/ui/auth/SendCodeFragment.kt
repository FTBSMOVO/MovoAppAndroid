package com.movocash.movo.view.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.SendVerificationCodeRequestModel
import com.movocash.movo.databinding.FragmentSendCodeBinding
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.AccountViewModel

class SendCodeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentSendCodeBinding
    private lateinit var accountViewModel: AccountViewModel

    companion object {
        lateinit var instance: SendCodeFragment
        private var isUserName = false

        fun newInstance(userName: Boolean): SendCodeFragment {
            instance = SendCodeFragment()
            isUserName = userName
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_code, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setView()
        setUiObserver()
        setListeners()
        return binding.root
    }

    private fun setView() {
        binding.isUserName = isUserName
    }

    private fun setUiObserver() {
        accountViewModel.codeSent.observe(viewLifecycleOwner, Observer {
            it?.let { msg ->
                addFragment(R.id.authContainer, VerifyCodeFragment.newInstance(binding.etUserName.text.toString()), "VerifyCodeFragment")
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
        binding.btnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnNext -> {
                if (!TextUtils.isEmpty(binding.etUserName.text.toString())) {
                    accountViewModel.sendForgotVerificationCode(SendVerificationCodeRequestModel(binding.etUserName.text.toString(), "", ""))
                } else {
                    binding.etUserName.requestFocus()
                    binding.etUserName.errorAnim(ActivityBase.activity)
                }
            }
        }
    }
}