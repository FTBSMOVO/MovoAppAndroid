package com.movocash.movo.view.ui.main.cashcard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentCashCardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.ICardClickListener
import com.movocash.movo.utilities.extensions.showAccountSelectionDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.cashcard.adapter.AdapterCashCards
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel

class CashCardFragment : BaseFragment(), View.OnClickListener, AdapterCashCards.ITransaction, ICardClickListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentCashCardBinding
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var referenceId: String
    private lateinit var cardCVV: String
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var sortList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var nList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var pList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    lateinit var adapterSearch: ArrayAdapter<*>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_card, container, false)
        binding.tvTitle.isSelected = true
        binding.isData = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        getCardsAccounts()
        setUiObserver()
        setListeners()
        return binding.root
    }
/*    private fun performSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (sortList.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(context, "No Match found", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }*/




    private fun getCardsAccounts() {
        cardsViewModel.getCardAccounts(!binding.srlCards.isRefreshing)

    }

    private fun setUiObserver() {
        cardsViewModel.cardAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlCards.isRefreshing = false
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.authFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.authResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj?.cardData != null && obj.cardData!!.cvV2 != null) {
                cardCVV = obj.cardData!!.cvV2!!
            }
        })

        cardsViewModel.cardAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { res ->
                binding.srlCards.isRefreshing = false
                if (res.obj != null && res.obj!!.cards != null && res.obj!!.cards!!.size > 0) {
                    saveCardsResponse(res)
                    cardList.clear()
                    cardList.addAll(res.obj!!.cards!!)
                    cardList1.addAll(cardList.filter { it.isPrimaryCardSpecified })
                    cardList1[0].isSelected = true
                    pList =
                        ArrayList((cardList.filter { it.isPrimaryCardSpecified && it.statusCode != "F" }))
                    if (cardList.any { it.isPrimaryCardSpecified }) {
                        //setCardData(cardList.filter { it.isPrimaryCardSpecified }[0] )
                        setCardData(pList[0])
                        cardsViewModel.getCardAuthData(cardList.filter { it.isPrimaryCardSpecified }[0].referenceID!!)
                    }

                    if (cardList.size > 1) {
                        if (cardList.any { !it.isPrimaryCardSpecified }) {
                            binding.isData = true
                            mList.clear()
                            /*mList.addAll(ArrayList(cardList.filter { !it.isPrimaryCardSpecified }))
                            mList.clear()
                            mList.addAll(ArrayList(cardList.filter { it.statusCode!="F"}))*/
                            mList.addAll(ArrayList(cardList.filter { !it.isPrimaryCardSpecified }))


                            setAdapter()
                        } else
                            binding.isData = false

                    } else {
                        binding.isData = false
                    }

                }
            }
        })
    }

    private fun saveCardsResponse(obj: GetCardAccountsResponseModel) {
        val cardList = obj.obj!!.cards

        var primaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        var secondaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        if (cardList!!.any { it.isPrimaryCardSpecified }) {
            primaryList = ArrayList(cardList.filter { it.isPrimaryCardSpecified })
        }
        if (cardList.any { !it.isPrimaryCardSpecified }) {
            secondaryList = ArrayList(cardList.filter { !it.isPrimaryCardSpecified })
        }

        if (primaryList.isNotEmpty()) {
            cardList.clear()
            cardList.addAll(primaryList)
        }

        if (secondaryList.isNotEmpty())
            cardList.addAll(secondaryList)

        obj.obj!!.cards = cardList
        MovoApp.db.putString(Constants.CONST_CARD_RESPONSE, Gson().toJson(obj))
    }

    private fun setCardData(obj: GetCardAccountsResponseModel.Card) {
        val cardNumber = "*" + obj.cardNumber!!.substring(obj.cardNumber!!.length - 4)
        val num = "${obj.programAbbreviation}${cardNumber}"
        binding.cardNum = num
        binding.cardAmount = obj.balance
        binding.isCardSet = true
        referenceId = obj.referenceID!!
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvCashCard.layoutManager = manager

        /*for ((index, value) in mList.withIndex()) {
            if(mList[index].statusCode == "F")
            {
               nList =  mList.drop()
            }

        }*/
       // mList.drop(0)
        //sortList = ArrayList(mList.sortedBy { it.programAbbreviation?.toString() })
        sortList = ArrayList(
            mList.sortedWith(
                compareBy(
                    String.CASE_INSENSITIVE_ORDER,
                    { it.programAbbreviation?.toString()!! })
            )
        )
        //sortList = ArrayList( mList.sortWith(String.CASE_INSENSITIVE_ORDER.toString()))



        val adapter = AdapterCashCards(sortList)
        //val adapter = AdapterCashCards(mList)
        binding.rvCashCard.adapter = adapter
        adapter.setMyListener(this)


            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter(newText)
                    return false
                }
            })

        binding.searchView.setOnSearchClickListener(android.view.View.OnClickListener {  } )



    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
       // binding.llAmount.setOnClickListener(this)
        binding.llEditCard.setOnClickListener(this)
        binding.srlCards.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlRight -> callFragmentWithReplace(
                R.id.mainContainer, AddCashCardFragment.newInstance(
                    true
                ), "AddCashCardFragment"
            )
            R.id.llAmount -> showAccountSelectionDialog(cardList, false, this)
            R.id.llEditCard -> ActivityBase.activity.showToastMessage("Under Development...")
        }
    }

    override fun onItemClicked(position: Int,sList: List<GetCardAccountsResponseModel.Card>) {
        callFragmentWithReplace(
            R.id.mainContainer,
            CashCardDetailFragment.newInstance(sList[position]),
            "CashCardDetailFragment"
        )
    }

    override fun onClickCard(position: Int) {
       // setCardData(position)

    }

    override fun onRefresh() {
        getCardsAccounts()
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


