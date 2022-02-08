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
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.LoadUnloadRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentReloadConfirmationBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel

class ReloadCardConfirmationFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentReloadConfirmationBinding
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var commonViewModel: CommonViewModel
    var num = ""

    companion object {
        lateinit var instance: ReloadCardConfirmationFragment
        lateinit var cardObj: GetCardAccountsResponseModel.Card
        lateinit var primaryCardObj: GetCardAccountsResponseModel.Card
        var amount = 0.0

        fun newInstance(obj: GetCardAccountsResponseModel.Card, pObj: GetCardAccountsResponseModel.Card, balance: Double): ReloadCardConfirmationFragment {
            instance = ReloadCardConfirmationFragment()
            cardObj = obj
            primaryCardObj = pObj
            amount = balance
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reload_confirmation, container, false)
        binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        getServiceFee()
        setUiObserver()
        setListeners()
        setViews()
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(primaryCardObj.referenceID!!, Constants.CONST_RELOAD_CASHCARD)
    }

    private fun setUiObserver() {

        commonViewModel.serviceFailure.observe(viewLifecycleOwner, Observer { msg ->
            getServiceFee()
        })

        commonViewModel.serviceFeeResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { data ->
                if (data.data != null && data.data!!.requestedServiceFee != null && !TextUtils.isEmpty(data.data!!.requestedServiceFee)) {
                    setFees(data.data!!.requestedServiceFee.toDouble())
                }
            }
        })

        cardsViewModel.loadUnloadCashCardFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.loadUnloadCardResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (!TextUtils.isEmpty(obj.data!!.responseDesc!!))
                showInfoDialog("", obj.data!!.responseDesc!!, this)
            else {
                ActivityBase.activity.showToastMessage("Card Reloaded Successfully")
                gotoHome()
                callFragment(R.id.mainContainer, CashCardFragment(), null)
            }
        })

    }

    private fun setViews() {
        val cardNumber = "*" + primaryCardObj.cardNumber!!.substring(primaryCardObj.cardNumber!!.length - 4)
        num = "${primaryCardObj.programAbbreviation}${cardNumber}"
        binding.fromCardNumber = num

        val cardNumber2 = "*" + cardObj.cardNumber!!.substring(cardObj.cardNumber!!.length - 4)
        val num2 = "${cardObj.programAbbreviation}${cardNumber2}"
        binding.toCardNumber = num2
        setFees(0.0)
    }

    private fun setFees(fee: Double) {
        binding.cardAmount = amount
        binding.fee = fee
        binding.totalAmount = amount + fee
        binding.notes = "NOTE: $${fee} will be deducted from card account $num"
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (binding.cardAmount!! + binding.fee!! < primaryCardObj.balance) {
                    cardsViewModel.reloadCashCard(LoadUnloadRequestModel(primaryCardObj.referenceID!!, cardObj.referenceID!!, amount))
                } else {
                    ActivityBase.activity.showLongToastMessage(Constants.INSUFFICIENT_FUND)
                }
            }
        }
    }

    override fun onClickOk() {
        gotoHome()
        callFragment(R.id.mainContainer, CashCardFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }
}