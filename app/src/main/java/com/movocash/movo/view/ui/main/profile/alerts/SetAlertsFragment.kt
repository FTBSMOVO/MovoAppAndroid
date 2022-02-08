package com.movocash.movo.view.ui.main.profile.alerts

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddUpdateUserAlerts
import com.movocash.movo.databinding.FragmentDebitAlertsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.utilities.helper.DecimalFilter
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.viewmodel.AccountViewModel

class SetAlertsFragment : BaseFragment(), View.OnClickListener, IInfoListener, BottomSheetSelectorFragment.ISelectListener, CompoundButton.OnCheckedChangeListener {

    lateinit var binding: FragmentDebitAlertsBinding
    private lateinit var accountViewModel: AccountViewModel
    var operatorID = 0
    private var operatorList: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    companion object {
        lateinit var instance: SetAlertsFragment
        lateinit var model: AddUpdateUserAlerts
        lateinit var cardNum: String
        lateinit var alertName: String
        lateinit var headerDescription: String
        var isEdit = false
        var isOperator = false

        fun newInstance(obj: AddUpdateUserAlerts, numb: String, isEditMode: Boolean, name: String, isOpt: Boolean, headerDesc: String): SetAlertsFragment {
            instance = SetAlertsFragment()
            model = obj
            cardNum = numb
            alertName = name
            isEdit = isEditMode
            isOperator = isOpt
            headerDescription = headerDesc
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debit_alerts, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.isOperator = isOperator
        binding.headerDescription = headerDescription
        binding.tvTitle.isSelected = true
        binding.cardNumber = cardNum
        binding.alertName = alertName
        if (isOperator)
            operatorID = 1

        if (!TextUtils.isEmpty(model.id)) {
            operatorID = model.operatorTypeId
            selectedPosition = if (model.operatorTypeId == Constants.CONST_OPERATOR_LESS) 0 else 1
            if (!TextUtils.isEmpty(model.email))
                binding.userEmail = model.email
            else
                binding.userEmail = MovoApp.db.getString(Constants.EMAIL)

            if (!TextUtils.isEmpty(model.sms))
                binding.userNumber = model.sms
            else
                binding.userNumber = MovoApp.db.getString(Constants.USER_NUMBER)

            binding.model = model
        } else {
            binding.model = null
            binding.userEmail = MovoApp.db.getString(Constants.EMAIL)
            binding.userNumber = MovoApp.db.getString(Constants.USER_NUMBER)
        }

    }

    private fun setUiObserver() {
        accountViewModel.alertSetFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        accountViewModel.alertSetSuccess.observe(viewLifecycleOwner, Observer {
            if (isEdit)
                showInfoDialog("Success", "Alert Set Successfully", this)
            else
                showInfoDialog("Success", "Alert Update Successfully", this)

        })
    }

    private fun setListeners() {
        binding.rlOperator.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.cbPhone.setOnCheckedChangeListener(this)
        binding.etAmount.addTextChangedListener(DecimalFilter(binding.etAmount,ActivityBase.activity))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlOperator -> showOperatorBottomSheet()
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (validateInput()) {
                    var email = ""
                    var phone = ""
                    if (binding.cbEmail.isChecked)
                        email = binding.etEmail.text.toString()
                    if (binding.cbPhone.isChecked)
                        phone = binding.etMobile.text.toString()

                    var amount = 0.00
                    if (binding.etAmount.text.toString().isNotEmpty())
                        amount = binding.etAmount.text.toString().toDouble()
                    accountViewModel.setAlert(AddUpdateUserAlerts(model.alertId, model.alertTypeId, model.id, operatorID, amount, phone, email, binding.cbPush.isChecked))
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        if (isOperator && TextUtils.isEmpty(binding.etAmount.text.toString())) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Amount")
            return false
        } else if (isOperator && binding.etAmount.text.toString().toDouble() == 0.0) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Invalid Amount")
            return false
        } else if (binding.cbEmail.isChecked && TextUtils.isEmpty(binding.etEmail.text.toString())) {
            binding.etEmail.requestFocus()
            binding.etEmail.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Email")
            return false
        } else if (binding.cbPhone.isChecked && TextUtils.isEmpty(binding.etMobile.text.toString())) {
            binding.etMobile.requestFocus()
            binding.etMobile.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Mobile")
            return false
        } else if (!isOperator && !binding.cbEmail.isChecked && !binding.cbPhone.isChecked && !binding.cbPush.isChecked) {
            ActivityBase.activity.showToastMessage("Please add at least 1 information")
            return false
        } else
            return true

    }

    private fun showOperatorBottomSheet() {
        operatorList.clear()
        operatorList.add("Less than or equal")
        operatorList.add("Greater than or equal")

        val bottomSheet = BottomSheetSelectorFragment(operatorList, selectedPosition, 1)
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onClickOk() {
        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onSelect(pos: Int, type: Int) {
        selectedPosition = pos - 1
        binding.tvOperator.text = operatorList[pos]
        setOperatorId()
    }

    private fun setOperatorId() {
        when (binding.tvOperator.text.toString()) {
            "Less than or equal" -> operatorID = Constants.CONST_OPERATOR_LESS
            "Greater than or equal" -> operatorID = Constants.CONST_OPERATOR_GREATER
        }
    }

    override fun onCancel(type: Int) {
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.cbPhone -> {
                if (isChecked)
                    binding.rlCharges.visibility = View.VISIBLE
                else
                    binding.rlCharges.visibility = View.GONE
            }
        }

    }
}