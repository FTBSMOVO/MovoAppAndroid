package com.movocash.movo.view.ui.main.movopay.sendmoney

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ShareFundRequestModel
import com.movocash.movo.databinding.FragmentSendMoneyDetailsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showLongToastMessage
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.paymenthistory.PaymentHistoryFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.movopay.history.HistoryFragment
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel

class SendMoneyConfirmationFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentSendMoneyDetailsBinding
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var commonViewModel: CommonViewModel

    companion object {
        lateinit var instance: SendMoneyConfirmationFragment
        lateinit var cardNum: String
        lateinit var requestModel: ShareFundRequestModel
        private var primaryAmount = 0.0

        fun newInstance(cardNumber: String, obj: ShareFundRequestModel, balance: Double): SendMoneyConfirmationFragment {
            instance = SendMoneyConfirmationFragment()
            cardNum = cardNumber
            requestModel = obj
            primaryAmount = balance
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_money_details, container, false)
        binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        getServiceFee()
        setUiObserver()
        setListeners()
        setViews(0.0)
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(requestModel.fromReferenceId, Constants.CONST_SHARE_FUNDS)
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

        cardsViewModel.shareFundFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.shareFundResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (!TextUtils.isEmpty(obj.data!!.responseDesc!!)) {
                MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")

                showInfoDialog("", obj.data!!.responseDesc!!, this)

            } else {
                /**track the event when the fund transfer succesfull*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_FT_MP_SEND_MONEY,
                    paramBundle
                )

                ActivityBase.activity.showToastMessage("Funds Transferred Successfully")
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), null)
            }
        })

    }

    private fun setViews(fee: Double) {
        binding.cardNumber = cardNum
        binding.sendTo = requestModel.toPhoneOrEmail
        binding.cardAmount = requestModel.amount
        binding.fee = fee
        binding.totalAmount = requestModel.amount + fee
        binding.notes = "NOTE: $${fee}0 will be deducted from card account $cardNum"

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (binding.totalAmount!! < primaryAmount)
                    shareFunds()
                else
                    ActivityBase.activity.showLongToastMessage(Constants.INSUFFICIENT_FUND)
            }
        }
    }

    private fun shareFunds() {
        cardsViewModel.shareFund(requestModel)
    }

    override fun onClickOk() {
        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        callFragment(R.id.mainContainer, HistoryFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}