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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.LoadUnloadRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentUnloadCashCardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.lang.reflect.Type

class UnloadCashCardFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentUnloadCashCardBinding
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card
    private lateinit var cardsViewModel: CardViewModel
    var num = ""
    private lateinit var commonViewModel: CommonViewModel

    companion object {
        lateinit var instance: UnloadCashCardFragment
        lateinit var cardObj: GetCardAccountsResponseModel.Card

        fun newInstance(obj: GetCardAccountsResponseModel.Card): UnloadCashCardFragment {
            instance = UnloadCashCardFragment()
            cardObj = obj
            return instance
        }
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(cardObj.referenceID!!, Constants.CONST_UNLOAD_CASHCARD)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unload_cash_card, container, false)
        binding.tvTitle.isSelected = true
        binding.model = CashCardDetailFragment.cardObj
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        getServiceFee()
        setUiObserver()
        setListeners()
        checkAndSet()
        return binding.root
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
                ActivityBase.activity.showToastMessage("Card unloaded Successfully")
                gotoHome()
                callFragment(R.id.mainContainer, CashCardFragment(), null)
            }
        })

    }

    private fun checkAndSet() {
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            primaryCardObj = cardModel.obj!!.cards!!.filter { it.isPrimaryCardSpecified }[0]
        }

        setViews()
    }

    private fun setViews() {
        val cardNumber = "*" + primaryCardObj.cardNumber!!.substring(primaryCardObj.cardNumber!!.length - 4)
        val num1 = "${primaryCardObj.programAbbreviation}${cardNumber}"
        binding.toCardNumber = num1

        val cardNumber2 = "*" + cardObj.cardNumber!!.substring(cardObj.cardNumber!!.length - 4)
        num = "${cardObj.programAbbreviation}${cardNumber2}"
        binding.fromCardNumber = num

        setFees(0.0)
    }

    private fun setFees(fee: Double) {
        binding.cardAmount = cardObj.balance
        binding.fee = fee
        binding.totalAmount = cardObj.balance + fee
        binding.notes = "NOTE: $${binding.fee} will be deducted from card account $num"
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                cardsViewModel.unloadCashCard(LoadUnloadRequestModel(primaryCardObj.referenceID!!, cardObj.referenceID!!, cardObj.balance - 0.50))
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