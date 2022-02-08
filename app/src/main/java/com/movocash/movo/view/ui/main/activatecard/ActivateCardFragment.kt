package com.movocash.movo.view.ui.main.activatecard

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
import com.movocash.movo.R
import com.movocash.movo.databinding.FragmentActivateCardBinding
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.ActivateCardModelApi
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.repository.CardRepository
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.IInfoListener
import com.movocash.movo.utilities.extensions.errorAnim
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CardViewModel
import java.lang.reflect.Type

class ActivateCardFragment : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentActivateCardBinding
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var sortList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var pList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private lateinit var cardsViewModel: CardViewModel
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card
    private lateinit var accountViewModel: AccountViewModel
    private var referenceId = ""
    private var primaryAmount = 0.0
    private var transitionFlag = 0
    private lateinit var programID: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activate_card, container, false)
        binding.tvTitle.isSelected = true

        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        setListeners()
        setViews()
        setUiObserver()
        checkAndSet()
        return binding.root
    }

    private fun setUiObserver() {
        cardsViewModel.ActivateCardResponse.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()

                showInfoDialog("yes", res.messages.toString(), this)
            }
        })

        cardsViewModel.ActivateCardFailure.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()

                showInfoDialog("yes", res, this)
            }
        })

        accountViewModel.getProfileResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null) {

                    programID = obj.data!!.cardProgramID.toString()
                    transitionFlag = obj.data!!.transitionFlag!!.toInt()
                   /* MovoApp.db.putString(Constants.USER_NUMBER, obj.data!!.cellNumber!!)
                    MovoApp.db.putString(Constants.EMAIL, obj.data!!.email!!)

                    if (!TextUtils.isEmpty(obj.data!!.billingAddress1)) {
                        val shipAddress = "${obj.data!!.billingAddress1}, ${obj.data!!.city} , ${obj.data!!.billingStateCode},${obj.data!!.billingPostalCode} , USA"
                        MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, shipAddress)
                    } else
                        MovoApp.db.putString(Constants.CONST_MY_SHIPPING_INFO, "")*/

                    /*binding.model = obj.data
                    commonViewModel.getStateByCountry(233)*/
                }
            }
        })

    }

    private fun setViews() {

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
       // binding.rlDeposit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlDeposit -> ActivityBase.activity.showToastMessage("Under Development...")
            R.id.btnCancel ->  {


                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
                addFragment(R.id.mainContainer, MyCardsFragment(), null)
            }
            R.id.btnConfirm -> {

                if (validateInput()) {
                    cardsViewModel.activateCard(ActivateCardModelApi(primaryCardObj.referenceID!!))

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

    private fun checkAndSet() {
        //binding.isCardSet = false
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
            //setCardData(1)
            primaryCardObj = mList1[0]
            setPrimaryCardData(0)
        }
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
    /*

        binding.isCardSet = true*/

        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance
        binding.cardAmount = mList1[position].balance
        binding.cardNumber = num
    }

    private fun validateInput(): Boolean {
        if (!binding.cbActivateCard.isChecked) {
            binding.cbActivateCard.requestFocus()
            binding.cbActivateCard.errorAnim(ActivityBase.activity)
            //ActivityBase.activity.showToastMessage("Email cannot be empty")
            return false
        }
        else return true

    }

        override fun onClickOk() {

            ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
            addFragment(R.id.mainContainer, MyCardsFragment(), null)

    }

    override fun onClickCancel() {

        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
        addFragment(R.id.mainContainer, MyCardsFragment(), null)


    }

/*    private fun setList() {
        var primaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        var secondaryList = ArrayList<GetCardAccountsResponseModel.Card>()
        if (mList.any { it.isPrimaryCardSpecified }) {
            primaryList = ArrayList(mList.filter { it.isPrimaryCardSpecified })
        }
        if (mList.any { !it.isPrimaryCardSpecified }) {
            secondaryList = ArrayList(mList.filter { !it.isPrimaryCardSpecified })
            // sortList = ArrayList(secondaryList.sortedBy { it.programAbbreviation?.toString() })
            sortList = ArrayList(
                    secondaryList.sortedWith(
                            compareBy(
                                    String.CASE_INSENSITIVE_ORDER,
                                    { it.programAbbreviation?.toString()!! })
                    )
            )
            secondaryList = sortList
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
        // referenceID = mList[0].referenceID!!
        pList = ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode != "F" })
       // referenceID = pList[0].referenceID!!

    }*/
}