package com.movocash.movo.view.ui.main.echeckbook.schedulepayments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.databinding.FragmentScheduledPaymentHistoryDetailBinding
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.gotoHome
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.ECheckBookViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment

class SchedulePaymentDetailFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentScheduledPaymentHistoryDetailBinding
    private lateinit var viewModel: ECheckBookViewModel

    companion object {
        lateinit var instance: SchedulePaymentDetailFragment
        lateinit var model: CustomPaymentDetailModel
        lateinit var requestModel: MakeUpdatePaymentRequestModel

        fun newInstance(obj: CustomPaymentDetailModel, reqObj: MakeUpdatePaymentRequestModel): SchedulePaymentDetailFragment {
            instance = SchedulePaymentDetailFragment()
            model = obj
            requestModel = reqObj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scheduled_payment_history_detail, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        viewModel.cancelPaymentFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.cancelPaymentResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc))
                    showInfoDialog("", obj.data!!.responseDesc!!, this)

            }
        })
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = model
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlRight -> {
                addFragment(R.id.mainContainer, UpdateSchedulePayment.newInstance(requestModel), "UpdateSchedulePayment")
            }
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnCancel -> viewModel.cancelPayment(requestModel.tansId)
        }
    }

    override fun onClickOk() {
        gotoHome()
        callFragment(R.id.mainContainer, SchedulePaymentsFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}