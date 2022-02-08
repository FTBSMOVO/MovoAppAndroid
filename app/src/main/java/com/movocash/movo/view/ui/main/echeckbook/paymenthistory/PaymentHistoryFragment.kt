package com.movocash.movo.view.ui.main.echeckbook.paymenthistory

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetPaymentHistoryResponseModel
import com.movocash.movo.databinding.FragmentPaymentHistoryBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.ICardClickListener
import com.movocash.movo.utilities.extensions.showAccountSelectionDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.adapter.AdapterPaymentHistory
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.ECheckBookViewModel
import java.lang.reflect.Type

class PaymentHistoryFragment : BaseFragment(), View.OnClickListener, AdapterPaymentHistory.IPayHistory, SwipeRefreshLayout.OnRefreshListener, ICardClickListener {

    lateinit var binding: FragmentPaymentHistoryBinding
    private var mList = ArrayList<CustomPaymentDetailModel>()
    private var mainList = ArrayList<GetPaymentHistoryResponseModel.Transaction>()
    private lateinit var viewModel: ECheckBookViewModel
    private var sendFromAccount = ""
    lateinit var adapter: AdapterPaymentHistory

    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""
    private lateinit var cardsViewModel: CardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_history, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUiObserver()
        checkAndSetCards()
        checkAndSet()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.isData = true
        binding.tvTitle.isSelected = true
        viewModel.getPaymentHistory(referenceId,!binding.srlHistory.isRefreshing)
    }

    private fun checkAndSetCards() {
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            val cardNumber = "*" + cardModel.obj!!.cards!![0].cardNumber!!.substring(cardModel.obj!!.cards!![0].cardNumber!!.length - 4)
            sendFromAccount = "${cardModel.obj!!.cards!![0].programAbbreviation}${cardNumber}"
        }
    }

    private fun setUiObserver() {
        viewModel.paymentHistoryFailure.observe(viewLifecycleOwner, Observer {
            binding.srlHistory.isRefreshing = false
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.paymentHistoryResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.srlHistory.isRefreshing = false
                if (obj.data != null && obj.data!!.transactions != null && obj.data!!.transactions!!.size > 0) {
                    //if (obj.data!!.transactions!!.any { it.status == Constants.PAYMENT_FAILED || it.status == Constants.PAYMENT_CANCELLED  || it.status == Constants.PAYMENT_POSTED || it.status == Constants.PAYMENT_RETURNED }) {
                        mList.clear()
                        mainList.clear()
                        mainList.addAll(obj.data!!.transactions!!)
                        obj.data!!.transactions!!//.filter { it.status == Constants.PAYMENT_FAILED || it.status == Constants.PAYMENT_CANCELLED || it.status == Constants.PAYMENT_POSTED || it.status == Constants.PAYMENT_RETURNED }
                                .mapTo(mList) {
                                    CustomPaymentDetailModel(sendFromAccount, it.payeeName!!, it.scheduledDate!!, it.amount, 0.0, 0.0, it.status!!, "", "${it.payeeCity}, ${it.payeeState}", Constants.CONST_TEXT, it.billPaymentTransId!!, it.payeeAccountNumber!!)
                                }
                            setAdapter()
                            setDate()
                            binding.isData = true
                   /* } else {
                        binding.isData = false
                    }*/

                } else {
                    binding.isData = false
                }

            }
        })
    }

    private fun setDate() {
        val groupList = mList.groupBy { it.transferDate.split("T")[0] }
        for (i in 0 until groupList.size) {
            val indexOfDateType = mList.indexOfFirst { it.transferDate.split("T")[0] == ArrayList(groupList.keys)[i] && it.type == Constants.CONST_DATE }
            val index = mList.indexOfFirst { it.transferDate.split("T")[0] == ArrayList(groupList.keys)[i] }
            if (indexOfDateType >= 0) {
                mList.removeAt(indexOfDateType)
            }
            mList.add(index, CustomPaymentDetailModel("", "", ArrayList(groupList.keys)[i], 0.0, 0.0, 0.0, "", "", "", Constants.CONST_DATE, "", ""))
        }
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvHistory.layoutManager = manager
        adapter = AdapterPaymentHistory(mList)
        binding.rvHistory.adapter = adapter
        adapter.setMyListener(this)
    }


    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.llAmount.setOnClickListener(this)
        binding.srlHistory.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.llAmount -> {
                if (cardList.size > 1)
                    showAccountSelectionDialog(cardList, false, this)
            }
        }
    }

    override fun onClickItem(position: Int) {
        val obj = mList[position]
        var obj2 = GetPaymentHistoryResponseModel.Transaction()
        if (mainList.any { it.billPaymentTransId == mList[position].paymentId }) {
            obj2 = mainList.filter { it.billPaymentTransId == mList[position].paymentId }[0]
            obj.address = "${obj2.payeeAddress}, ${obj2.payeeCity}, ${obj2.payeeState}, ${obj2.payeeZip}"
        }
        addFragment(R.id.mainContainer, PaymentHistoryDetailFragment.newInstance(obj), "PaymentHistoryDetailFragment")
    }

    override fun onRefresh() {
        viewModel.getPaymentHistory(referenceId,!binding.srlHistory.isRefreshing)
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
            cardList.clear()
            cardList = cardModel.obj!!.cards!!
            cardList1 = ArrayList(cardList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
            setPrimaryCardData(0)
        }
    }

    private fun setCardData(position: Int) {
        cardList[position].isSelected = true
        val cardNumber = "*" + cardList[position].cardNumber!!.substring(cardList[position].cardNumber!!.length - 4)
        val num = "${cardList[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList[position].balance
        binding.isCardSet = true
        referenceId = cardList[position].referenceID!!
        viewModel.getPaymentHistory(referenceId, !binding.srlHistory.isRefreshing)
    }

    private fun setPrimaryCardData(position: Int) {
        cardList1[position].isSelected = true
        val cardNumber = "*" + cardList1[position].cardNumber!!.substring(cardList1[position].cardNumber!!.length - 4)
        val num = "${cardList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList1[position].balance
        binding.isCardSet = true
        referenceId = cardList1[position].referenceID!!
        viewModel.getPaymentHistory(referenceId, !binding.srlHistory.isRefreshing)
    }

    override fun onClickCard(position: Int) {

        setCardData(position)
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