package com.movocash.movo.view.ui.main.movocash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.movocash.movo.data.model.requestmodel.CustomHistoryModel
import com.movocash.movo.data.model.requestmodel.ReissueCardModelApi
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentCardTransactionsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showPlasticCardDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.adapter.AdapterCardTransactions
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CardViewModel
import java.lang.reflect.Type

class CardTransactionsFragment : BaseFragment(), View.OnClickListener, IInfoListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentCardTransactionsBinding
    private var mList = ArrayList<CustomHistoryModel>()
    lateinit var adapter: AdapterCardTransactions
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var accountViewModel: AccountViewModel
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card

    companion object {
        lateinit var instance: CardTransactionsFragment
        lateinit var referenceId: String
        lateinit var cardNumber: String
        var availableBalance: Double = 0.0
        private var isPrimary = false

        fun newInstance(refId: String, cardNum: String, amount: Double, isPrimaryCard: Boolean): CardTransactionsFragment {
            instance = CardTransactionsFragment()
            referenceId = refId
            cardNumber = cardNum
            availableBalance = amount
            isPrimary = isPrimaryCard
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_transactions, container, false)
        binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        setListeners()
        setViews()
        return binding.root
    }

    private fun setViews() {
        binding.isData = true
        binding.cardNum = cardNumber
        binding.cardAmount = availableBalance
        cardsViewModel.getTransactionHistory(referenceId, !binding.srlTrans.isRefreshing)
    }

    private fun setUiObserver() {
        cardsViewModel.transactionHistoryFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlTrans.isRefreshing = false
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.transactionHistoryResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { res ->
                binding.srlTrans.isRefreshing = false
                if (res.data != null && res.data!!.statement != null && res.data!!.statement!!.transactions != null && res.data!!.statement!!.transactions!!.size > 0) {
                    mList.clear()
                    res.data!!.statement!!.transactions!!.mapTo(mList) {
                        CustomHistoryModel(it.amount, it.description!!, it.transDate!!, Constants.CONST_TEXT)
                    }
                   // binding.isData = true
//                    if (isPrimary)
                        //binding.btnOrderCard.visibility = View.VISIBLE
                    setAdapter()
                    setDate()
                } else {
                    //binding.btnOrderCard.visibility = View.GONE
                    //binding.isData = false
                }
            }
        })


        accountViewModel.reissueCardMessage.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()

                showInfoDialog("yes", res, this)
            }
        })
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvTransactionHistory.layoutManager = manager
        adapter = AdapterCardTransactions(mList)
        binding.rvTransactionHistory.adapter = adapter
    }


    private fun setDate() {
        val groupList = mList.groupBy { it.date.split("T")[0] }
        for (i in 0 until groupList.size) {
            val indexOfDateType = mList.indexOfFirst { it.date.split("T")[0] == ArrayList(groupList.keys)[i] && it.type == Constants.CONST_DATE }
            val index = mList.indexOfFirst { it.date.split("T")[0] == ArrayList(groupList.keys)[i] }
            if (indexOfDateType >= 0) {
                mList.removeAt(indexOfDateType)
            }
            mList.add(index, CustomHistoryModel(0.0, "", ArrayList(groupList.keys)[i], Constants.CONST_DATE)
            )
        }
        adapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnOrderCard.setOnClickListener(this)
        binding.srlTrans.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            R.id.rlRight -> {
            }
            R.id.btnOrderCard -> showPlasticCardDialog(this)
        }
    }

    override fun onClickOk() {

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
        accountViewModel.reIssueCard(ReissueCardModelApi(primaryCardObj.cardNumber!!))

    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        cardsViewModel.getTransactionHistory(referenceId, !binding.srlTrans.isRefreshing)
    }
}