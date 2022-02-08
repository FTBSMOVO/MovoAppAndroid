package com.movocash.movo.view.ui.main.lockunlock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentLockUnlockCardBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.CardViewModel
import java.lang.reflect.Type

class LockUnLockCardFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentLockUnlockCardBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private lateinit var cardsViewModel: CardViewModel
    var referenceId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lock_unlock_card, container, false)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        setUiObserver()
        setViews()
        setListeners()
        return binding.root
    }

    private fun setUiObserver() {
        cardsViewModel.blockedFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.blockedResponseModel.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage("Card Blocked Successfully")

        })

        cardsViewModel.unBlockedResponseModel.observe(viewLifecycleOwner, Observer {
            ActivityBase.activity.showToastMessage("Card Unblocked Successfully")
        })

        cardsViewModel.cardAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)
        })
        cardsViewModel.cardAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { res ->
                if (res.obj != null && res.obj!!.cards != null && res.obj!!.cards!!.size > 0) {
                    mList.clear()
                    mList.addAll(res.obj!!.cards!!)
                    setList()
                    res.obj!!.cards = mList
                    MovoApp.db.putString(Constants.CONST_CARD_RESPONSE, Gson().toJson(res))
                    checkAndSetCards()
                }
            }
        })
    }

    private fun setList() {
        var primaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        var secondaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        if (mList.any { it.isPrimaryCardSpecified }) {
            primaryList = ArrayList(mList.filter { it.isPrimaryCardSpecified })
        }
        if (mList.any { !it.isPrimaryCardSpecified }) {
            secondaryList = ArrayList(mList.filter { !it.isPrimaryCardSpecified })
        }

        if (primaryList.isNotEmpty()) {
            mList.clear()
            primaryList.map { it.type = Constants.CONST_PRIMARY }
            mList.addAll(primaryList)

        }

        if (secondaryList.isNotEmpty()) {
            secondaryList.map { it.type = Constants.CONST_SECONDARY }
            mList.addAll(secondaryList)
        }
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        cardsViewModel.getCardAccounts(true)
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
        referenceId = mList[position].referenceID!!
        binding.cbCard.isChecked = mList[position].statusCode != "B"
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNum = num
        referenceId = mList1[position].referenceID!!
        binding.cbCard.isChecked = mList1[position].statusCode != "B"
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.cbCard.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.cbCard -> {
                if (binding.cbCard.isChecked) {
                    cardsViewModel.blockCard(referenceId)
                } else {
                    cardsViewModel.unBlockCard(referenceId)
                }
            }
        }
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