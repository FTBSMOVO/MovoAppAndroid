package com.movocash.movo.view.ui.main.echeckbook.makepayment

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
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetMyPayeeResponseModel
import com.movocash.movo.databinding.FragmentMakePaymentBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.analytics.CustomEventNames
import com.movocash.movo.utilities.analytics.CustomEventParams
import com.movocash.movo.utilities.analytics.FirebaseAnalyticsEventHelper
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.utilities.helper.DecimalFilter
import com.movocash.movo.utilities.utils.DateUtil
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.bottomsheet.BottomSheetDatePickerFragment
import com.movocash.movo.view.ui.bottomsheet.BottomSheetSelectorFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.ECheckBookViewModel
import java.lang.reflect.Type

class MakePaymentFragment : BaseFragment(), View.OnClickListener, ICardClickListener, BottomSheetDatePickerFragment.ISelectDobListener, BottomSheetSelectorFragment.ISelectListener {

    private var paymentDate: String? = null
    lateinit var binding: FragmentMakePaymentBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private lateinit var viewModel: ECheckBookViewModel
    private var referenceId = ""
    private var primaryAmount = 0.0
    private var isPayeeFound = false
    private var payeeList: ArrayList<GetMyPayeeResponseModel.Payee> = ArrayList()
    private var myPayeeList: ArrayList<String> = ArrayList()
    private var selectedPosition = 0
    private var payeeAccountNumber = ""

    companion object {
        lateinit var instance: MakePaymentFragment
        private var payeeSrNo = ""
        var isFromSide = false

        fun newInstance(srNo: String, isSide: Boolean): MakePaymentFragment {
            instance = MakePaymentFragment()
            payeeSrNo = srNo
            isFromSide = isSide
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_payment, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        //checkAndSetCards()
        checkAndSet()
        checkAndSetPayee()
        return binding.root
    }

    private fun setUiObserver() {
        viewModel.myPayeeFailure.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.myPayeeResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj?.data != null && obj.data!!.payees != null && obj.data!!.payees!!.size > 0) {
                    binding.llAddPayee.isEnabled = true
                    MovoApp.db.putString(Constants.CONST_MY_PAYEE_RESPONSE, Gson().toJson(obj))
                    checkAndSetPayee()
                }

            }
        })
    }

    private fun checkAndSetPayee() {
        binding.isPayeeSet = false
        var payeeModel: GetMyPayeeResponseModel? = null
        val listType: Type = object : TypeToken<GetMyPayeeResponseModel>() {}.type
        payeeModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_MY_PAYEE_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (payeeModel != null) {
            if (payeeModel.data != null && payeeModel.data!!.payees != null && payeeModel.data!!.payees!!.size > 0) {
                isPayeeFound = true
                payeeList.clear()
                payeeList.addAll(payeeModel.data!!.payees!!)

                if (!isFromSide) {
                    if (payeeList.any { it.payeeSerialNo == payeeSrNo }) {
                        val index = payeeList.indexOf(payeeList.filter { it.payeeSerialNo == payeeSrNo }[0])
                        selectedPosition = index
                        setPayee(index)
                    } else {
                        setPayee(0)
                    }
                } else {
                    setPayee(0)
                }
            } else {
                isPayeeFound = false
            }
        } else {
            binding.llAddPayee.isEnabled = false
            getMyPayees()
        }
    }

    private fun setPayee(position: Int) {
        binding.isPayeeSet = true
        binding.payeeName = payeeList[position].payeeName!!
        payeeSrNo = payeeList[position].payeeSerialNo!!
        payeeAccountNumber = payeeList[position].payeeAccountNumber!!
    }

    private fun getMyPayees() {
        viewModel.getMyPayeesList(false)
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        if (!isFromSide) {
            binding.rlLeftBack.visibility = View.VISIBLE
            binding.tvMiniTitle.visibility = View.VISIBLE
            binding.rlLeftSide.visibility = View.GONE
            binding.tvTitle.text = "My Payee"
        } else {
            binding.rlLeftBack.visibility = View.GONE
            binding.tvMiniTitle.visibility = View.GONE
            binding.rlLeftSide.visibility = View.VISIBLE
            binding.tvTitle.text = "Make Payment"
        }
    }

 /*   private fun checkAndSetCards() {
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
            setCardData(0)
        }
    }*/

 /*   private fun setCardData(position: Int) {
        val cardNumber = "*" + mList[position].cardNumber!!.substring(mList[position].cardNumber!!.length - 4)
        val num = "${mList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList[position].balance
        binding.isCardSet = true
        referenceId = mList[position].referenceID!!
        primaryAmount = mList[position].balance
    }*/


    private fun setListeners() {
        binding.rlLeftSide.setOnClickListener(this)
        binding.rlLeftBack.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.llMoney.setOnClickListener(this)
        binding.llAddPayee.setOnClickListener(this)
        binding.llDate.setOnClickListener(this)
        binding.etAmount.addTextChangedListener(DecimalFilter(binding.etAmount,ActivityBase.activity))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeftSide -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlLeftBack -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
                if (validateInput()) {
                    proceedToMakePayment()
                }
            }
            R.id.llMoney -> {
                if (mList.size > 1)
                    showAccountSelectionDialog(mList, false, this)
            }
            R.id.llAddPayee -> {
                if (isPayeeFound) {
                    if (payeeList.size > 1) {
                        showPayeeBottomSheet()
                    }
                } else
                    showSelectPayeeDialog()
            }
            R.id.llDate -> showDateDialog()
        }
    }

    private fun proceedToMakePayment() {
        val obj = MakeUpdatePaymentRequestModel(referenceId,"", payeeSrNo, payeeAccountNumber, binding.etAmount.text.toString().toDouble(), paymentDate!!, binding.etComments.text.toString())
        val customObj = CustomPaymentDetailModel(binding.cardNum!!, binding.payeeName!!, paymentDate!!, binding.etAmount.text.toString().toDouble(), 0.0, 0.0, "", "", "", 0, "", "")
        addFragment(R.id.mainContainer, PaymentConfirmationFragment.newInstance(customObj, obj, isFromSide,primaryAmount), "PaymentConfirmationFragment")
    }

    private fun showPayeeBottomSheet() {
        if (myPayeeList.size > 0)
            myPayeeList.clear()
        myPayeeList.addAll(ArrayList(payeeList.map { it.payeeName!! }))

        if (myPayeeList.size > 0) {
            val bottomSheet = BottomSheetSelectorFragment(myPayeeList, selectedPosition, 1)
            if (!bottomSheet.isAdded)
                bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
            bottomSheet.setMyListener(this)
        }
    }

    private fun validateInput(): Boolean {
        if (!binding.isPayeeSet!!) {
            ActivityBase.activity.showToastMessage("Please Select Payee")
            return false
        } else if (!binding.isCardSet!!) {
            ActivityBase.activity.showToastMessage("Please Select Card")
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
        } else if (binding.etAmount.text.toString().toDouble() >= binding.cardAmount!!) {
            binding.etAmount.requestFocus()
            binding.etAmount.errorAnim(ActivityBase.activity)
            ActivityBase.activity.showToastMessage("The amount you are requesting to transfer is greater than your available balance.")
            return false
        } else if (TextUtils.isEmpty(binding.tvDate.text.toString())) {
            ActivityBase.activity.showToastMessage("Please Select Payment Date")
            return false
        } else
            return true
    }

    private fun showDateDialog() {
        val bottomSheet: BottomSheetDatePickerFragment = if (paymentDate != null) {
            BottomSheetDatePickerFragment(paymentDate!!, Constants.CONST_SCHEDULE)
        } else {
            BottomSheetDatePickerFragment("", Constants.CONST_SCHEDULE)
        }
        if (!bottomSheet.isAdded)
            bottomSheet.show(ActivityBase.activity.supportFragmentManager, bottomSheet.tag)
        bottomSheet.setMyListener(this)
    }

    override fun onSelectDob(dob: String) {
        paymentDate = dob
        binding.tvDate.text = DateUtil.setAppFormatDate(paymentDate!!)
    }

    override fun onSelectDob(dob: String, view: View) {
        TODO("Not yet implemented")
    }

    override fun onDateCancel() {

    }

    override fun onClickCard(position: Int) {
        setCardData(position)
    }

    override fun onSelect(pos: Int, type: Int) {
        selectedPosition = pos - 1
        setPayee(selectedPosition)
    }

    override fun onCancel(type: Int) {
    }

    ///////////////////////////////////
    private fun checkAndSet() {
        binding.isData = true
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
            setPrimaryCardData(0)
        }
    }

    private fun setCardData(position: Int) {
        mList[position].isSelected = true
        val cardNumber = "*" + mList[position].cardNumber!!.substring(mList[position].cardNumber!!.length - 4)
        val num = "${mList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList[position].balance
        binding.isCardSet = true
        referenceId = mList[position].referenceID!!
        primaryAmount = mList[position].balance
        //bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)

        if(mList[position].isPrimaryCardSpecified){
            val paramBundle = Bundle()
            paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
            FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                requireContext(),
                CustomEventNames.EVENT_PRIMARY_MOVO_CASH_ACCOUNT_MAKE_PAYMENT,
                paramBundle
            )
        }else{
            val paramBundle = Bundle()
            paramBundle.putString(CustomEventParams.SCREEN_NAME, javaClass.simpleName)
            FirebaseAnalyticsEventHelper.trackAnalyticEvent(
                requireContext(),
                CustomEventNames.EVENT_CASH_CARD_MAKE_PAYMENT,
                paramBundle
            )
        }
    }
    private fun setPrimaryCardData(position: Int) {
        mList1[position].isSelected = true
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = mList1[position].balance
        binding.isCardSet = true
        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance
        //bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
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