package com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer

import android.content.Context
import android.os.Bundle
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
import com.movocash.movo.data.model.requestmodel.CustomCashOutBankModel
import com.movocash.movo.data.model.requestmodel.CustomScheduleTransferModels
import com.movocash.movo.data.model.requestmodel.MyCustomScheduleTransferModels
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetScheduledTransfersResponseModel
import com.movocash.movo.databinding.FragmentScheduleTransferBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.ICardClickListener
import com.movocash.movo.utilities.extensions.showAccountSelectionDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.digitalbanking.cashoutbank.CashOutBankDetailFragment
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.adapter.AdapterScheduledTransfers
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.movocash.movo.viewmodel.CardViewModel
import java.lang.reflect.Type

class ScheduleTransferFragment : BaseFragment(), View.OnClickListener, AdapterScheduledTransfers.IScheduleInterface, SwipeRefreshLayout.OnRefreshListener, ICardClickListener {

    lateinit var binding: FragmentScheduleTransferBinding
    private lateinit var bankViewModel: BankViewModel
    private var mainList: ArrayList<GetScheduledTransfersResponseModel.SingleTransfer> = ArrayList()
    private var mList: ArrayList<MyCustomScheduleTransferModels> = ArrayList()
    lateinit var adapter: AdapterScheduledTransfers
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""

    private lateinit var cardsViewModel: CardViewModel

  /*  companion object {
        lateinit var instance: ScheduleTransferFragment
        //lateinit var model: CustomCashOutBankModel

       *//* fun newInstance(obj: CustomCashOutBankModel): CashOutBankDetailFragment {
            instance = CashOutBankDetailFragment()
            model = obj
            return instance
        }*//*
    }*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_transfer, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUIObserver()

        checkAndSet()

      //ActivityBase.activity.showToastMessage("Token : "+Constants.ACCESS_TOKEN)
        setViews()
        setListeners()
        return binding.root
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.isData = true
        bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
       // ActivityBase.activity.showToastMessage("reference id : "+referenceId)

       // bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
    }

    private fun setUIObserver() {
        bankViewModel.scheduleTransferListFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlTransfers.isRefreshing = false
            ActivityBase.activity.showToastMessage("error : "+msg)
           // ActivityBase.activity.showToastMessage("failure")
        })

        bankViewModel.scheduleTransferListResponseModel.observe(viewLifecycleOwner, Observer {
            binding.srlTransfers.isRefreshing = false
            it?.let { obj ->
                //ActivityBase.activity.showToastMessage("Success")
                if (obj?.data != null && obj.data!!.singleTransfers != null && obj.data!!.singleTransfers!!.size > 0) {
                    if (obj.data!!.singleTransfers!!.any { it.status == Constants.PAY_SCHEDULED || it.status == Constants.PAY_SCHEDULED || it.status == Constants.PAY_IN_PROGRESS }) {
                        mainList.clear()
                        mainList.addAll(obj.data!!.singleTransfers!!.filter { it.status == Constants.PAY_SCHEDULED || it.status == Constants.PAY_SCHEDULED || it.status == Constants.PAY_IN_PROGRESS })
                        mList.clear()
                        for (i in 0 until mainList.size) {
                            val cardNumber = "*" + mainList[i].transferFrom!!.substring(mainList[i].transferFrom!!.length - 4)
                            val num = "${MovoApp.db.getString(Constants.LAST_NAME)}${cardNumber}"
                            val bankNumber = "*" + mainList[i].transferTo!!.substring(mainList[i].transferTo!!.length - 4)
                            val bank = "${MovoApp.db.getString(Constants.LAST_NAME)}${bankNumber}"

                            val customObj = MyCustomScheduleTransferModels(num, bank, (mainList[i].amount!!.toDouble()), mainList[i].transferDate!!, mainList[i].transferId!!, Constants.CONST_TEXT, mainList[i].status!!,mainList[i].achType)
                            mList.add(customObj)
                        }
                        binding.isData = true
                        setAdapter()
                        setDate()
                    } else
                        binding.isData = false

                } else
                    binding.isData = false
            }
        })
    }

    private fun setDate() {
        val groupList = mList.groupBy { it.date.split("T")[0] }
        for (i in 0 until groupList.size) {
            val indexOfDateType = mList.indexOfFirst { it.date.split("T")[0] == ArrayList(groupList.keys)[i] && it.type == Constants.CONST_DATE }
            val index = mList.indexOfFirst { it.date.split("T")[0] == ArrayList(groupList.keys)[i] }
            if (indexOfDateType >= 0) {
                mList.removeAt(indexOfDateType)
            }
            mList.add(index, MyCustomScheduleTransferModels("", "", 0.0, ArrayList(groupList.keys)[i], "", Constants.CONST_DATE, "",1))
        }
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvTransfers.layoutManager = manager
        adapter = AdapterScheduledTransfers(mList)
        binding.rvTransfers.adapter = adapter
        adapter.setListener(this)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.llAmount.setOnClickListener(this)
        binding.srlTransfers.setOnRefreshListener(this)
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
        val customObj = mList[position]
        val mainObj = mainList.filter { it.transferId == customObj.transferId }[0]
        addFragment(R.id.mainContainer, ScheduleTransferDetailFragment.newInstance(customObj, mainObj), "ScheduleTransferDetailFragment")
    }

    override fun onRefresh() {
        bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
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
        bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
    }

    private fun setPrimaryCardData(position: Int) {
        cardList1[position].isSelected = true
        val cardNumber = "*" + cardList1[position].cardNumber!!.substring(cardList1[position].cardNumber!!.length - 4)
        val num = "${cardList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList1[position].balance
        binding.isCardSet = true
        referenceId = cardList1[position].referenceID!!
        bankViewModel.getScheduleTransferList(referenceId,!binding.srlTransfers.isRefreshing)
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