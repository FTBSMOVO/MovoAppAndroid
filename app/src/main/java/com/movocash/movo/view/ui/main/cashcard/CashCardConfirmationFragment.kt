package com.movocash.movo.view.ui.main.cashcard

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
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddCashCardRequestModel
import com.movocash.movo.databinding.FragmenttAddCashCardConfirmationBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel

class CashCardConfirmationFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmenttAddCashCardConfirmationBinding
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var commonViewModel: CommonViewModel

    companion object {
        lateinit var instance: CashCardConfirmationFragment
        lateinit var cardNum: String
        lateinit var requestModel: AddCashCardRequestModel
        var isFromMain = false
        var primaryAmount = 0.0

        fun newInstance(cardNumber: String, obj: AddCashCardRequestModel, isMain: Boolean, amount: Double): CashCardConfirmationFragment {
            instance = CashCardConfirmationFragment()
            cardNum = cardNumber
            requestModel = obj
            isFromMain = isMain
            primaryAmount = amount
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmentt_add_cash_card_confirmation, container, false)
       // binding.model = CashCardDetailFragment.cardObj
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)

        getServiceFee()
        setUiObserver()
        setListeners()
        setViews(0.0)
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(requestModel.fromReferenceId, Constants.CONST_GENERATE_CASHCARD)
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
        cardsViewModel.addCashCardFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })


        cardsViewModel.addCashCardFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.addCashCardResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (!TextUtils.isEmpty(obj.data!!.responseDesc!!))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
            else {
                /**track event when add cash card successfull*/
                val paramBundle = Bundle()
                paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
                FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                    requireContext(),
                    CustomEventNames.EVENT_CREATE_CASH_CARD,
                    paramBundle
                )

                ActivityBase.activity.showToastMessage("Funds Transferred Successfully")
                if (isFromMain) {
                    ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                } else {
                    ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                }
            }
        })

    }

    private fun setViews(fee: Double) {
        binding.tvTitle.isSelected = true
        binding.cardNumber = cardNum
        binding.cardAmount = requestModel.amount
        binding.fee = fee
        binding.totalAmount = requestModel.amount + fee
        binding.notes = "NOTE: $${binding.fee} will be deducted from card account $cardNum"

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft ->
            {
                //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                gotoHome()
                callFragment(R.id.mainContainer, CashCardFragment(), null)
            }
            R.id.rlRight -> {
                if (binding.cardAmount!! + binding.fee!! < primaryAmount) {
                    shareFunds()
                } else {
                    ActivityBase.activity.showLongToastMessage(Constants.INSUFFICIENT_FUND)
                }
            }
        }
    }

    private fun shareFunds() {
        cardsViewModel.addCashCard(requestModel)
    }

    override fun onClickOk() {
        if (isFromMain) {
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        } else {
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}