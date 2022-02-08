package com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomEditScheduledTransferModel
import com.movocash.movo.data.model.requestmodel.CustomScheduleTransferModels
import com.movocash.movo.data.model.responsemodel.GetScheduledTransfersResponseModel
import com.movocash.movo.databinding.FragmentScheduleTransfersDetailsBinding
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.gotoHome
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.CashOutBankFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.data.model.requestmodel.MyCustomScheduleTransferModels
import com.movocash.movo.utilities.Constants
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.cashintoMovo

class ScheduleTransferDetailFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentScheduleTransfersDetailsBinding
    private lateinit var bankViewModel: BankViewModel

    companion object {
        lateinit var instance: ScheduleTransferDetailFragment
        lateinit var customobj: MyCustomScheduleTransferModels
        lateinit var mainObj: GetScheduledTransfersResponseModel.SingleTransfer

        fun newInstance(obj1: MyCustomScheduleTransferModels, obj2: GetScheduledTransfersResponseModel.SingleTransfer): ScheduleTransferDetailFragment {
            instance = ScheduleTransferDetailFragment()
            customobj = obj1
            mainObj = obj2
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_transfers_details, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        setUIObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUIObserver() {
        bankViewModel.cancelScheduledTransferFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.cancelScheduledTransferResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj != null && obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                    showInfoDialog("", obj.data!!.responseDesc!!, this)
                }
            }
        })
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.model = customobj
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.btnCancel ->
            {
                if(customobj.achType == Constants.BANK_TO_CARD_TRANSFER.toLong())
                {
                    bankViewModel.cancelBtcScheduledTransfer(customobj.transferId)
                }
                else
                bankViewModel.cancelScheduledTransfer(customobj.transferId)
            }
            R.id.rlRight -> gotoEditFlow()
        }
    }

    private fun gotoEditFlow() {
        val obj = CustomEditScheduledTransferModel(mainObj.transferId!!, mainObj.transferFrom!!, mainObj.transferTo!!, mainObj.amount!!.toDouble(), mainObj.transferDate!!, mainObj.frequencyType, mainObj.transferComments!!)
       if(mainObj.achType == Constants.CARD_TO_BANK_TRANSFER.toLong())
        addFragment(R.id.mainContainer, CashOutBankFragment.newInstance(obj), "CashOutBankFragment")
        else
           addFragment(R.id.mainContainer, cashintoMovo.newInstance(obj), "cashintoMovo")

    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, ScheduleTransferFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}