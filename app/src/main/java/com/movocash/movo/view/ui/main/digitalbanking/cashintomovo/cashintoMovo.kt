package com.movocash.movo.view.ui.main.digitalbanking.cashoutbank

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CashOutToBankRequestModel
import com.movocash.movo.data.model.requestmodel.CustomCashOutBankModel
import com.movocash.movo.data.model.requestmodel.CustomEditScheduledTransferModel
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentCashOutBankBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.utils.DateUtil
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetDatePickerFragment
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.ScheduleTransferFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.constants
import com.movocash.movo.data.model.requestmodel.CashintoMovoRequestModel
import com.movocash.movo.databinding.FragmentCashintoMovoBinding
import com.movocash.movo.utilities.Constants.BANK_TO_CARD_TRANSFER
import com.movocash.movo.utilities.helper.DecimalFilter

import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import java.lang.reflect.Type

class cashintoMovo : BaseFragment(), View.OnClickListener, BottomSheetSelectorFragment.ISelectListener, IFrequencyList, BottomSheetDatePickerFragment.ISelectDobListener, ICardClickListener, IInfoListener {

    lateinit var binding: FragmentCashintoMovoBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var bankList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()
    private var plaidList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()
    private var referenceId = ""
    private var bankSrNo = ""
    private var isBankFound = false
    private var myBankList: ArrayList<String> = ArrayList()
    private var selectedPosition = 0
    private var selectedFrequency = 0
    private var scheduleDate: String? = null
    private lateinit var bankViewModel: BankViewModel

    companion object {
        lateinit var instance: cashintoMovo
        var model: CustomEditScheduledTransferModel? = null

        fun newInstance(obj: CustomEditScheduledTransferModel?): cashintoMovo {
            instance = cashintoMovo()
            model = obj
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashinto_movo, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        bankViewModel.getBankAccounts(true)
        setUIObserver()
        setViews()
        setListeners()
        checkAndSetCards()
      //  checkAndSetBanks()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        if (model != null) {
            binding.tvRight.text = "Confirm"
            binding.etAmount.setText(model!!.amount.toString())
            binding.tvTitle.text = "Scheduled Transfers"
            binding.tvMiniTitle.visibility = View.VISIBLE
            binding.rlLeftBack.visibility = View.VISIBLE
            binding.rlLeftSide.visibility = View.GONE
            scheduleDate = model!!.date
            if (DateUtil.checkBefore(scheduleDate!!) || DateUtil.checkIsToday(scheduleDate!!)) {
                selectedFrequency = Constants.CONST_FREQUENCY_ONCE
            } else {
                selectedFrequency = Constants.CONST_FREQUENCY_DATE
            }
            binding.tvScheduleDate.text = DateUtil.setAppFormatDate(scheduleDate!!)
            when (selectedFrequency) {
                Constants.CONST_FREQUENCY_ONCE -> {
                    binding.tvSelectFrequency.text = "One Time, Right Now"
                    binding.rlDate.visibility = View.GONE
                }
                Constants.CONST_FREQUENCY_DATE -> {
                    binding.tvSelectFrequency.text = "One Time, Specific Date"
                    binding.rlDate.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUIObserver() {
        bankViewModel.getBankAccountsFailure.observe(viewLifecycleOwner, Observer { msg ->
        })

        bankViewModel.getBankAccountsResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj != null && obj.data != null && obj.data!!.accounts != null && obj.data!!.accounts!!.size > 0) {
                binding.llSendTo.isEnabled = true
                MovoApp.db.putString(Constants.CONST_MY_BANKS_RESPONSE, Gson().toJson(obj))
                ///////////////
                /*myBankList.clear()
                mList.addAll(obj.data!!.accounts!!)*/

                /////////////////
                checkAndSetBanks()
            }
        })

        bankViewModel.bankToCashTransferFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.bankToCashTransferResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj != null && obj.data != null && obj.data!!.responseDesc != null && !TextUtils.isEmpty(obj.data!!.responseDesc)) {
                    showInfoDialog("", obj.data!!.responseDesc!!, this)
                }
            }
        })
    }

    private fun setListeners() {
        binding.rlLeftBack.setOnClickListener(this)
        binding.rlLeftSide.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.llFromAccount.setOnClickListener(this)
        binding.llSendTo.setOnClickListener(this)
        binding.llFrequency.setOnClickListener(this)
        binding.llSchedule.setOnClickListener(this)
        binding.etAmount.addTextChangedListener(DecimalFilter(binding.etAmount,ActivityBase.activity))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeftSide -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlLeftBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (validateInput()) {
                    if (model != null)
                        proceedToEdit()
                    else
                        proceedToConfirmation()
                }
            }
            R.id.llFromAccount -> {
                showAccountSelectionDialogClosedCards(mList, false, this)
            }
            R.id.llSendTo -> {
                if (isBankFound) {
                    if (bankList.size > 0) {
                        showBankBottomSheet()
                    }
                } else {
                    showSelectBankDialog()
                }
            }
            R.id.llFrequency -> {
                showSelectFrequencyDialog(this)
            }
            R.id.llSchedule -> showDateDialog()
        }
    }

    private fun checkAndSetBanks() {
        binding.isBankSet = false
        var bankModel: GetBankAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetBankAccountsResponseModel>() {}.type
        bankModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_MY_BANKS_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }
        if (bankModel != null) {
            if (bankModel.data != null && bankModel.data!!.accounts != null && bankModel.data!!.accounts!!.size > 0) {
                isBankFound = true
                bankList.clear()
                bankList = bankModel.data!!.accounts!!

                plaidList = bankList.filter { it.achType==1 } as java.util.ArrayList<GetBankAccountsResponseModel.Account>
                bankList = plaidList

                if (model != null) {
                    if (bankList.any { it.accountNumber == model!!.toAccount }) {
                        val ind = bankList.indexOf(bankList.filter { it.accountNumber == model!!.toAccount }[0])
                       // setBankData(ind)
                    } else
                    {
                        // setBankData(0)
                    }


                } else
                {
                   // setBankData(0)
                }

            } else
                isBankFound = false
        } else {
            binding.llSendTo.isEnabled = false
            bankViewModel.getBankAccounts(false)
        }


    }

    private fun setBankData(position: Int) {
        var bankNum = ""
        if (bankList[position].accountNumber!!.length > 5) {
            bankNum = "*" + bankList[position].accountNumber!!.substring(bankList[position].accountNumber!!.length - 4)
        }
        val data = "${bankList[position].accountNickname}${bankNum}"
        binding.bankNum = data
        binding.isBankSet = true
        bankSrNo = bankList[position].accountSrNo!!
    }

    private fun checkAndSetCards() {
        binding.isCardSet = false
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

    private fun setCardData(position: Int) {
        val cardNumber = "*" + mList[position].cardNumber!!.substring(mList[position].cardNumber!!.length - 4)
        val num = "${mList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList[position].balance
        binding.isCardSet = true
        referenceId = mList[position].referenceID!!
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList1[position].balance
        binding.isCardSet = true
        referenceId = mList1[position].referenceID!!
    }



    private fun proceedToEdit() {

        bankViewModel.bankToCashTransfer(CashintoMovoRequestModel(model!!.transferId, bankSrNo, binding.etAmount.text.toString().toDouble(), binding.etComments.text.toString(), model!!.frequencyType, model!!.date))

        //bankViewModel.cashToBankTransfer(CashOutToBankRequestModel(model!!.transferId, bankSrNo, binding.etAmount.text.toString().toDouble(), binding.etComments.text.toString(), model!!.frequencyType, model!!.date))
    }

    private fun proceedToConfirmation() {
        if (selectedFrequency == Constants.CONST_FREQUENCY_ONCE) {
            scheduleDate = DateUtil.getCurrentDate()
        }

        if (!scheduleDate!!.contains("T"))
            scheduleDate = "${scheduleDate}T00:00:00.000Z"

       /* val obj = CashintoMovoRequestModel(model!!.transferId, bankSrNo, binding.etAmount.text.toString().toDouble(), binding.etComments.text.toString(), model!!.frequencyType, model!!.date)
        addFragment(R.id.mainContainer, cashintoMovoDetailFragment.newInstance(obj), "CashOutBankDetailFragment")*/
        val obj = CustomCashOutBankModel(bankSrNo, binding.bankNum!!, referenceId, binding.cardNum!!, selectedFrequency, scheduleDate!!, binding.etAmount.text.toString().toDouble(), 0.0, 0.0, binding.etComments.text.toString(), "")
        addFragment(R.id.mainContainer, cashintoMovoDetailFragment.newInstance(obj), "CashOutBankDetailFragment")

    }

    private fun validateInput(): Boolean {
        if (!binding.isCardSet!!) {
            ActivityBase.activity.showToastMessage("Please Select a Card")
            return false
        } else if (!binding.isBankSet!!) {
            ActivityBase.activity.showToastMessage("Please Select a Bank")
            return false
        } else if (TextUtils.isEmpty(binding.etAmount.text.toString())) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            return false
        }  else if (binding.etAmount.text.toString().toDouble() == 0.0) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("Please Enter Valid Amount")
            return false
        }/* else if (binding.etAmount.text.toString().toDouble() >= binding.cardAmount!!) {
            ActivityBase.activity.showToastMessage("The amount you are requesting to transfer is greater than your available balance.")
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            return false
        }*/ else if (selectedFrequency == 0) {
            ActivityBase.activity.showToastMessage("Please Select Frequency")
            return false
        } else if (selectedFrequency == Constants.CONST_FREQUENCY_DATE && TextUtils.isEmpty(binding.tvScheduleDate.text.toString())) {
            ActivityBase.activity.showToastMessage("Please Select Scheduled Date")
            return false
        } else
            return true
    }

    private fun showDateDialog() {
        val bottomSheet: BottomSheetDatePickerFragment = if (scheduleDate != null) {
            BottomSheetDatePickerFragment(scheduleDate!!, Constants.CONST_SCHEDULE)
        } else {
            BottomSheetDatePickerFragment("", Constants.CONST_SCHEDULE)
        }
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    private fun showBankBottomSheet() {
        if (myBankList.size > 0)
            myBankList.clear()
        var bankNum = ""

        for (i in 0 until bankList.size) {
            if (bankList[i].accountNumber!!.length > 5) {
                bankNum = "*" + bankList[i].accountNumber!!.substring(bankList[i].accountNumber!!.length - 4)
            }
            val data = "${bankList[i].accountNickname}${bankNum}"
            myBankList.add(data)
        }

        if (myBankList.size > 0) {
            val bottomSheet = BottomSheetSelectorFragment(myBankList, selectedPosition, 1)
            if (!bottomSheet.isAdded)
                bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
            bottomSheet.setMyListener(this)
        }


    }

    override fun onSelect(pos: Int, type: Int) {
        selectedPosition = pos - 1
        setBankData(selectedPosition)
    }

    override fun onCancel(type: Int) {
    }

    override fun onItemSelect(type: Int) {
        selectedFrequency = type
        when (type) {
            Constants.CONST_FREQUENCY_ONCE -> {
                binding.tvSelectFrequency.text = "One Time, Right Now"
                binding.rlDate.visibility = View.GONE
            }
            Constants.CONST_FREQUENCY_DATE -> {
                binding.tvSelectFrequency.text = "One Time, Specific Date"
                binding.rlDate.visibility = View.VISIBLE
            }
        }
    }

    override fun onSelectDob(dob: String) {
        scheduleDate = dob
        binding.tvScheduleDate.text = DateUtil.setAppFormatDate(scheduleDate!!)
    }

    override fun onSelectDob(dob: String, view: View) {
        TODO("Not yet implemented")
    }

    override fun onDateCancel() {

    }

    override fun onClickCard(position: Int) {
        setCardData(position)
    }

    override fun onClickOk() {
        gotoHome()
        callFragmentWithReplace(R.id.mainContainer, ScheduleTransferFragment(), null)
    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}