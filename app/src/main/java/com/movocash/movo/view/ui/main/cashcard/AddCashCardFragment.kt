package com.movocash.movo.view.ui.main.cashcard

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.AddCashCardRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmenttAddCashCardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.utilities.helper.DecimalFilter
import java.lang.reflect.Type

class AddCashCardFragment : BaseFragment(), View.OnClickListener, ICardClickListener {

    lateinit var binding: FragmenttAddCashCardBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""
    private var primaryAmount = 0.0

    companion object {
        lateinit var instance: AddCashCardFragment
        var isFromMain = false

        fun newInstance(isMain: Boolean): AddCashCardFragment {
            instance = AddCashCardFragment()
            isFromMain = isMain
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmentt_add_cash_card, container, false)
        binding.tvTitle.isSelected = true
        checkAndSet()
        setListeners()
        return binding.root
    }

    private fun checkAndSet() {
        binding.isCardSet = false
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            mList.clear()
            mList = cardModel.obj!!.cards!!

            mList1 = ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
           // mList = ArrayList(mList.filter {  it.statusCode!="F"})
            setPrimaryCardData(0)
        }
    }

    private fun setCardData(position: Int) {
        val cardNumber = "*" + mList[position].cardNumber!!.substring(mList[position].cardNumber!!.length - 4)
        val num = "${mList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList[position].balance
        binding.isCardSet = true
        referenceId = mList[position].referenceID!!
        primaryAmount = mList[position].balance!!
    }
    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList1[position].balance
        binding.isCardSet = true
        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance!!
    }

    private fun setListeners() {
        //binding.llMoney.setOnClickListener(this)
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.etAmount.addTextChangedListener(DecimalFilter(binding.etAmount,ActivityBase.activity))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llMoney -> showAccountSelectionDialog(mList, false, this)
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (validateInput()) {
                    binding.root.hideKeyboard()
                    val obj = AddCashCardRequestModel(referenceId, binding.etAmount.text.toString().toDouble(), binding.etName.text.toString())
                    addFragment(R.id.mainContainer, CashCardConfirmationFragment.newInstance(binding.cardNum!!, obj, isFromMain,primaryAmount), "SendMoneyDetailsFragment")
                }
            }
        }
    }

    override fun onClickCard(position: Int) {
        setCardData(position)
    }

    private fun validateInput(): Boolean {
        if (!binding.isCardSet!!) {
            ActivityBase.activity.showToastMessage("Please Select A card")
            return false
        } else if (TextUtils.isEmpty(binding.etAmount.text.toString())) {
            /*binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)*/
            binding.etAmount.setText("0.00")
            return true
        }  /*else if (binding.etAmount.text.toString().toDouble() == 0.0) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Valid Amount")
            return false
        }*/ else if (binding.etAmount.text.toString().toDouble() >= binding.cardAmount!!) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("The amount you are requesting to transfer is greater than your available balance.")
            return false
        } else {
            return true
        }
    }
}