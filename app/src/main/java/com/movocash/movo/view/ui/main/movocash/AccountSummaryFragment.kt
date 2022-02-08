package com.movocash.movo.view.ui.main.movocash

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
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetMiniStatementResponseModel
import com.movocash.movo.databinding.FragmentAccountsSummaryBinding
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.showPlasticCardDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.adapter.AdapterRecentTransaction
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.ReissueCardModelApi
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.ReissueCardModel
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.view.ui.auth.SignInFragment
import com.movocash.movo.viewmodel.AccountViewModel
import java.lang.reflect.Type

class AccountSummaryFragment : BaseFragment(), View.OnClickListener, AdapterRecentTransaction.ITransaction, IInfoListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentAccountsSummaryBinding
    var mList: ArrayList<GetMiniStatementResponseModel.Transaction> = ArrayList()
    private var pList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var pList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    var depositList: ArrayList<GetMiniStatementResponseModel.Transaction> = ArrayList()
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var accountViewModel: AccountViewModel
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_accounts_summary, container, false)
        binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        cardsViewModel.getMiniStatementAccounts(!binding.srlSummary.isRefreshing)
        setUiObserver()
        setListeners()
        primaryCard()
        return binding.root
    }

    private fun setUiObserver() {
        cardsViewModel.miniStatementFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlSummary.isRefreshing = false
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.miniStatementResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { res ->
                binding.srlSummary.isRefreshing = false
                if (res.data != null && res.data!!.statement != null && res.data!!.statement!!.transactions != null && res.data!!.statement!!.transactions!!.size > 0) {
                    binding.rlNoDeposit.visibility = View.GONE
                    binding.rlNoTransaction.visibility = View.GONE
                    binding.rvTransaction.visibility = View.VISIBLE

                    mList.clear()
                    mList.addAll(res.data!!.statement!!.transactions!!)
                    setAdapter()

                    if (mList.any { it.amount > 0.0 }) {
                        binding.rvRecentDeposit.visibility = View.VISIBLE
                        depositList.clear()
                        depositList.addAll(ArrayList(mList.filter { it.amount > 0.0 }))
                        setDepositAdapter()
                    } else {
                        binding.rlNoDeposit.visibility = View.VISIBLE
                        binding.rvRecentDeposit.visibility = View.GONE
                    }
                    //binding.btnOrderCard.visibility = View.VISIBLE
                } else {
                    binding.rlNoDeposit.visibility = View.VISIBLE
                    binding.rlNoTransaction.visibility = View.VISIBLE
                    binding.rvRecentDeposit.visibility = View.GONE
                   // binding.btnOrderCard.visibility = View.GONE
                    binding.rvTransaction.visibility = View.GONE
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
        binding.rvTransaction.layoutManager = manager
        val adapter = AdapterRecentTransaction(mList)
        binding.rvTransaction.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setDepositAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvRecentDeposit.layoutManager = manager
        val adapter = AdapterRecentTransaction(depositList)
        binding.rvRecentDeposit.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
      //  binding.btnOrderCard.setOnClickListener(this)
        binding.srlSummary.setOnRefreshListener(this)
        binding.tvViewAll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.btnOrderCard -> {
                showPlasticCardDialog( this)
            }
            R.id.tvViewAll -> {
                //callFragmentWithReplace(R.id.mainContainer, CardTransactionsFragment(), null)
                val cardNumber = "*" + pList1[0].cardNumber!!.substring(pList1[0].cardNumber!!.length - 4)
                val num = "${pList1[0].programAbbreviation}${cardNumber}"
                addFragment(
                    R.id.mainContainer, CardTransactionsFragment.newInstance(
                        pList1[0].referenceID!!,
                        num,
                        pList1[0].balance,
                         true
                    ), "CardTransactionsFragment"
                )
            }


        }
    }

    override fun onItemClicked(position: Int) {

    }

    override fun onClickOk() {
        //Toast.makeText(activity, "Confirm Clicked", Toast.LENGTH_LONG).show()
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            pList.clear()
            pList = cardModel.obj!!.cards!!

            pList1 = ArrayList(pList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
        }


    }

    private fun primaryCard()
    {
        var cardModel: GetCardAccountsResponseModel? = null
        val listType: Type = object : TypeToken<GetCardAccountsResponseModel>() {}.type
        cardModel = try {
            Gson().fromJson(MovoApp.db.getString(Constants.CONST_CARD_RESPONSE), listType)
        } catch (e: Exception) {
            null
        }

        if (cardModel?.obj != null && cardModel.obj!!.cards != null && cardModel.obj!!.cards!!.size > 0) {
            pList.clear()
            pList = cardModel.obj!!.cards!!

            pList1 = ArrayList(pList.filter { it.isPrimaryCardSpecified && it.statusCode!="F"})
        }


    }

    override fun onClickCancel() {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        cardsViewModel.getMiniStatementAccounts(!binding.srlSummary.isRefreshing)
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