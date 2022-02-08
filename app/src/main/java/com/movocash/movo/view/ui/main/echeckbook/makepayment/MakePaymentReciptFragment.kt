package com.movocash.movo.view.ui.main.echeckbook.makepayment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.databinding.FragmentMakePaymentReciptBinding
import com.movocash.movo.databinding.FragmentPaymentConfirmationBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.mypayee.MyPayeesFragment
import com.movocash.movo.view.ui.main.echeckbook.schedulepayments.SchedulePaymentsFragment
import com.movocash.movo.viewmodel.CommonViewModel
import com.movocash.movo.viewmodel.ECheckBookViewModel

class MakePaymentReciptFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentMakePaymentReciptBinding
    private lateinit var viewModel: ECheckBookViewModel
    private lateinit var commonViewModel: CommonViewModel

    companion object {
        lateinit var instance: MakePaymentReciptFragment
        lateinit var customObj: CustomPaymentDetailModel
        lateinit var requestObj: MakeUpdatePaymentRequestModel
        var isFromSide = false
        var primaryAmount = 0.00

        fun newInstance(obj1: CustomPaymentDetailModel, obj2: MakeUpdatePaymentRequestModel, isSide: Boolean, amount:Double): MakePaymentReciptFragment {
            instance = MakePaymentReciptFragment()
            customObj = obj1
            requestObj = obj2
            isFromSide = isSide
            primaryAmount = amount
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_payment_recipt, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        getServiceFee()
        setUiObserver()
        setViews(0.0)
        setListeners()
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(MovoApp.db.getString(Constants.PRIMARY_REF_ID)!!, Constants.CONST_MAKE_PAYMENT)
    }

    private fun setUiObserver() {
        commonViewModel.serviceFailure.observe(viewLifecycleOwner, Observer { msg ->
            getServiceFee()
        })

        commonViewModel.serviceFeeResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { data ->
                if (data.data != null && data.data!!.requestedServiceFee != null && !TextUtils.isEmpty(data.data!!.requestedServiceFee)) {
                    setViews(data.data!!.requestedServiceFee.toDouble())
                }
            }
        })

        viewModel.makeUpdatePaymentFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.makeUpdatePaymentResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj?.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                    MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")

                    showInfoDialog("", obj.data!!.responseDesc!!, this)
                }

            }
        })
    }

    private fun setViews(fee: Double) {
        if (!isFromSide) {
            binding.tvTitle.text = "My Payee"
            binding.tvMiniTitle.visibility = View.VISIBLE
        } else {
            binding.tvTitle.text = "Make Payment"
            binding.tvMiniTitle.visibility = View.GONE
        }
        binding.tvTitle.isSelected = true
        customObj.fee = fee
        customObj.totalAmount = fee + customObj.amount
        customObj.notes = "NOTE: $${fee} will be deducted from card account ${customObj.fromAccount}"
        binding.model = customObj
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnViewhistory.setOnClickListener(this)
        binding.btnAnotherTransfer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (customObj.totalAmount <= primaryAmount) {
                    viewModel.makeUpdatePayment(requestObj)
                } else {
                    ActivityBase.activity.showLongToastMessage(Constants.INSUFFICIENT_FUND)
                    ActivityBase.activity.showLongToastMessage(customObj.toAccount.toString())
                    ActivityBase.activity.showLongToastMessage(primaryAmount.toString())
                }
            }
            R.id.btnViewhistory -> {
                callFragment(R.id.mainContainer, SchedulePaymentsFragment(), null)
            }
            R.id.btnAnotherTransfer -> {

                callFragment(R.id.mainContainer, MakePaymentFragment(), null)
            }
        }
    }

    override fun onClickOk() {
        gotoHome()
        if (isFromSide)
            callFragment(R.id.mainContainer, SchedulePaymentsFragment(), null)
        else
            callFragment(R.id.mainContainer, MyPayeesFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}