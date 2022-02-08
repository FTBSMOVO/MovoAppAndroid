package com.movocash.movo.view.ui.main.digitalbanking.cashoutbank

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CashOutToBankRequestModel
import com.movocash.movo.data.model.requestmodel.CustomCashOutBankModel
import com.movocash.movo.databinding.FragmentCashOutToBankDetailBinding
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.gotoHome
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.ScheduleTransferFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.viewmodel.CommonViewModel
import java.lang.reflect.Type

class CashOutBankDetailFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentCashOutToBankDetailBinding
    private lateinit var bankViewModel: BankViewModel
    private lateinit var commonViewModel: CommonViewModel
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""

    companion object {
        lateinit var instance: CashOutBankDetailFragment
        lateinit var model: CustomCashOutBankModel

        fun newInstance(obj: CustomCashOutBankModel): CashOutBankDetailFragment {
            instance = CashOutBankDetailFragment()
            model = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_out_to_bank_detail, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        getServiceFee()
        setUIObserver()
        setViews(2.0)
        checkAndSetCards()
        setListeners()
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(MovoApp.db.getString(Constants.PRIMARY_REF_ID)!!, Constants.CONST_CASH_TO_BANK)
    }

    private fun setViews(fee:Double) {
        binding.tvTitle.isSelected = true
        model.fee = fee
        model.totalAmount = model.fee + model.cardAmount
        model.notes = "NOTE: $${model.fee + model.cardAmount} will be deducted from card account ${model.cardNum}"
        binding.model = model
    }

    private fun setUIObserver() {
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

        bankViewModel.cashToBankTransferFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.cashToBankTransferResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj != null && obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                    MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")

                    //showInfoDialog("", obj.data!!.responseDesc!!, this)
                   // callFragmentWithReplace(R.id.mainContainer, CashOutBankRecipt(), null)

                   // val obj = CustomCashOutBankModel(bankSrNo, binding.bankNum!!, referenceId, binding.cardNum!!, selectedFrequency, scheduleDate!!, binding.etAmount.text.toString().toDouble(), 0.0, 0.0, binding.etComments.text.toString(), "")
                    addFragment(R.id.mainContainer, CashOutBankRecipt.newInstance(model), "CashOutBankDetailFragment")
                }
            }
        })
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight ->{
                bankViewModel.cashToBankTransfer(CashOutToBankRequestModel(referenceId,"", model.srNo, model.cardAmount, model.comments, model.frequencyType, model.date))
               // Toast.makeText(context,referenceId+" "+ model.srNo+" "+model.cardAmount+" "+ model.comments+" "+model.frequencyType+" "+ model.date,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, ScheduleTransferFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    private fun checkAndSetCards() {
       // binding.isCardSet = false
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        /*  if (cardList.any { it.isPrimaryCardSpecified }) {
              primaryCardList = ArrayList(cardList.filter { it.isPrimaryCardSpecified  && it.statusCode!="F"})
          }*/
        //mList.addAll(ArrayList(cardList.filter { !it.isPrimaryCardSpecified && it.statusCode!="F"}))
        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            mList.clear()
            mList = cardModel.obj!!.cards!!
            mList1 = ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
            setPrimaryCardData(0)
        }
    }
    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"

        referenceId = mList1[position].referenceID!!
    }
}