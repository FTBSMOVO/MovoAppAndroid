package com.movocash.movo.view.ui.main.movopay.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.movocash.movo.data.model.requestmodel.CustomShareHistoryModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentHistoryBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.ICardClickListener
import com.movocash.movo.utilities.extensions.showAccountSelectionDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.movopay.history.adapter.AdapterHistory
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import java.lang.reflect.Type
import java.util.*

class HistoryFragment : BaseFragment(), View.OnClickListener, ICardClickListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentHistoryBinding
    private var mList = ArrayList<CustomShareHistoryModel>()
    private var mList1 = ArrayList<CustomShareHistoryModel>()
    private lateinit var cardsViewModel: CardViewModel
    lateinit var adapter: AdapterHistory
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUiObserver()
        checkAndSet()
        setListeners()
        return binding.root
    }

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
        cardsViewModel.getShareHistory(referenceId, !binding.srlHistory.isRefreshing)
    }
    private fun setPrimaryCardData(position: Int) {
        cardList1[position].isSelected = true
        val cardNumber = "*" + cardList1[position].cardNumber!!.substring(cardList1[position].cardNumber!!.length - 4)
        val num = "${cardList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = cardList1[position].balance
        binding.isCardSet = true
        referenceId = cardList1[position].referenceID!!
        cardsViewModel.getShareHistory(referenceId, !binding.srlHistory.isRefreshing)
    }

    private fun setUiObserver() {
        cardsViewModel.shareHistoryFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlHistory.isRefreshing = false
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.shareHistoryResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            binding.srlHistory.isRefreshing = false
            obj?.let { res ->
                if (res.historyList != null && res.historyList!!.size > 0) {
                    mList.clear()
                    res.historyList!!.mapTo(mList) {
                        CustomShareHistoryModel(it.amount, it.label!!,it.payTo!!, it.createdOn!!, it.status!!, Constants.CONST_TEXT,it.comments!!)
                    }
                    binding.isData = true
                    setAdapter()
                    setDate()
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
            mList.add(index, CustomShareHistoryModel(
                amount = 0.0,
                label = "",
                sendTo = "",
                date = ArrayList(groupList.keys)[i],
                status = "",
                type = Constants.CONST_DATE,
                    comments = ""

                    )
            )
        }
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvHistory.layoutManager = manager
        adapter = AdapterHistory(mList)
        binding.rvHistory.adapter = adapter
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
//                    Toast.makeText(context,cardList.size,Toast.LENGTH_SHORT).show()
                    showAccountSelectionDialog(cardList, false, this)
            }
        }
    }

    override fun onClickCard(position: Int) {
        setCardData(position)
    }

    override fun onRefresh() {
        cardsViewModel.getShareHistory(referenceId, !binding.srlHistory.isRefreshing)
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