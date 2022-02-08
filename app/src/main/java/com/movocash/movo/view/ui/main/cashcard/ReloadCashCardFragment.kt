package com.movocash.movo.view.ui.main.cashcard

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentReloadCashCardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.hideKeyboard
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.utilities.helper.DecimalFilter
import java.lang.reflect.Type

class ReloadCashCardFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentReloadCashCardBinding
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()

    companion object {
        lateinit var instance: ReloadCashCardFragment
        lateinit var cardObj: GetCardAccountsResponseModel.Card

        fun newInstance(obj: GetCardAccountsResponseModel.Card): ReloadCashCardFragment {
            instance = ReloadCashCardFragment()
            cardObj = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reload_cash_card, container, false)
        binding.tvTitle.isSelected = true
        binding.model = CashCardDetailFragment.cardObj
        checkAndSet()
        setListeners()
        return binding.root
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
           // primaryCardObj = cardModel.obj!!.cards!!.filter { it.isPrimaryCardSpecified }[0]

            cardList.clear()
            cardList = cardModel.obj!!.cards!!
            cardList1 = ArrayList(cardList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
            primaryCardObj = cardList1[0]
        }

        setViews()
    }


   /* private fun setPrimaryCardData(position: Int) {
       // cardList1[position].isSelected = true
        val cardNumber = "*" + cardList1[position].cardNumber!!.substring(cardList1[position].cardNumber!!.length - 4)
        val num = "${cardList1[position].programAbbreviation}${cardNumber}"
        binding.fromCardNum = num
        binding.cardAmount = cardList1[position].balance
        binding.isCardSet = true
        referenceId = cardList1[position].referenceID!!
        viewModel.getPaymentHistory(referenceId, !binding.srlHistory.isRefreshing)
    }*/

    private fun setViews() {
        val cardNumber = "*" + primaryCardObj.cardNumber!!.substring(primaryCardObj.cardNumber!!.length - 4)
        val num = "${primaryCardObj.programAbbreviation}${cardNumber}"
        binding.fromCardNum = num

        val cardNumber2 = "*" + cardObj.cardNumber!!.substring(cardObj.cardNumber!!.length - 4)
        val num2 = "${cardObj.programAbbreviation}${cardNumber2}"
        binding.toCardNum = num2

        binding.cardAmount = primaryCardObj.balance
    }

    private fun setListeners() {
        binding.llMoney.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.etAmount.addTextChangedListener(DecimalFilter(binding.etAmount,ActivityBase.activity))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (validateInput()) {
                    binding.root.hideKeyboard()
                    addFragment(R.id.mainContainer, ReloadCardConfirmationFragment.newInstance(cardObj, primaryCardObj, binding.etAmount.text.toString().toDouble()), "ReloadCardConfirmationFragment")
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        if (TextUtils.isEmpty(binding.etAmount.text.toString())) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            return false
        } else if (binding.etAmount.text.toString().toDouble() == 0.0) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Valid Amount")
            return false
        }  else if (binding.etAmount.text.toString().toDouble() >= binding.cardAmount!!) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("The amount you are requesting to transfer is greater than your available balance.")
            return false
        } else {
            return true
        }
    }
}