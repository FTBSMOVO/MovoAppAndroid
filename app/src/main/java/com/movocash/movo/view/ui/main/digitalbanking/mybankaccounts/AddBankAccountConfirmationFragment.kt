package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.databinding.FragmentAddBankAccountConfirmationBinding
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter.BlankFragment
import com.movocash.movo.view.ui.main.profile.VerifyProfileCodeFragment
import com.movocash.movo.viewmodel.AccountViewModel

class AddBankAccountConfirmationFragment : BaseFragment(), View.OnClickListener, IInfoListener, IInfoListener2{

    lateinit var binding: FragmentAddBankAccountConfirmationBinding
    private lateinit var bankViewModel: BankViewModel
    private var isEdit = false
    private lateinit var accountViewModel: AccountViewModel

    companion object {
        lateinit var instance: AddBankAccountConfirmationFragment
        lateinit var requestObj: CreateBankAccountRequestModel

        fun newInstance(obj: CreateBankAccountRequestModel): AddBankAccountConfirmationFragment {
            instance = AddBankAccountConfirmationFragment()
            requestObj = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_bank_account_confirmation, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUIObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = requestObj
        isEdit = !TextUtils.isEmpty(requestObj.bankSerialNumberIfEdit)
        //requestObj.ac

        if (isEdit) {
            /*binding.tvType.visibility = View.GONE
            binding.btnDelete.visibility = View.VISIBLE
            binding.tvSide.text = "Edit"*/
            binding.tvType.visibility = View.GONE
            binding.btnDelete.visibility = View.VISIBLE
            binding.tvSide.text = ""
        } else {
            binding.tvType.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.GONE
            binding.tvSide.text = "Confirm"
        }
    }

    private fun setUIObserver() {
        bankViewModel.createBankAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.createBankAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
        })

        bankViewModel.removeBankAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.removeBankAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
        })

        accountViewModel.bankCodeSent.observe(viewLifecycleOwner, Observer {
            //callFragmentWithReplace(R.id.mainContainer, bankOtpFragment.newInstance(requestObj), "bankOtpFragment")
        })
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnDelete ->
            {
                confirmToggledialog2("","Are you sure you want to delete this Bank Account",this)
                //bankViewModel.removeBankAccount(requestObj.bankSerialNumberIfEdit)
            }
            R.id.rlRight -> {
                if (!isEdit) {
                    // bankViewModel.createBankAccount(requestObj)
                    // addFragment(R.id.mainContainer, bankOtpFragment(), null)

                    accountViewModel.sendBankOtp()
                    callFragmentWithReplace(R.id.mainContainer, bankOtpFragment.newInstance(requestObj), "bankOtpFragment")

                   // callFragmentWithReplace(R.id.mainContainer, bankOtpFragment(), "asdsa")
                }
                else {
                    addFragment(R.id.mainContainer, AddBankAccountFragment.newInstance(requestObj.accountType, true, requestObj), "AddBankAccountFragment")
                }
            }
        }
    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, MyBankAccountFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onClickOk2() {
        bankViewModel.removeBankAccount(requestObj.bankSerialNumberIfEdit)
    }

    override fun onClickCancel2() {

        ActivityBase.activity.showToastMessage("")
    }
}