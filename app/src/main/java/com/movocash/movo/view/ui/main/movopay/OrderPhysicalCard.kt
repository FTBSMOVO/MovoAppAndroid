package com.movocash.movo.view.ui.main.movopay


import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.ReissueCardModelApi
import com.movocash.movo.data.model.requestmodel.ShareFundRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentOrderPhysicalCardBinding
import com.movocash.movo.databinding.FragmentSendMoneyDetailsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.*
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.cashcard.UnloadCashCardFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.profile.ProfileFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.CommonViewModel
import java.lang.reflect.Type

class OrderPhysicalCard : BaseFragment(), View.OnClickListener, IInfoListener {

    lateinit var binding: FragmentOrderPhysicalCardBinding
    private lateinit var cardsViewModel: CardViewModel
    private lateinit var commonViewModel: CommonViewModel
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    lateinit var primaryCardObj: GetCardAccountsResponseModel.Card
    private lateinit var accountViewModel: AccountViewModel
    private var referenceId = ""
    private var primaryAmount = 0.0

    companion object {
        lateinit var instance: OrderPhysicalCard
        lateinit var cardNum: String
        lateinit var requestModel: ShareFundRequestModel
        private var primaryAmount = 0.0

        fun newInstance(cardNumber: String, obj: ShareFundRequestModel, balance: Double): OrderPhysicalCard {
            instance = OrderPhysicalCard()
            cardNum = cardNumber
            requestModel = obj
            primaryAmount = balance
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_physical_card, container, false)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        /*binding.tvTitle.isSelected = true
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)



        setListeners()
        setViews(0.0)*/

        setViews(0.0)
        setUiObserver()
        setListeners()
        checkAndSet()
        return binding.root
    }

    private fun getServiceFee() {
        commonViewModel.getServiceFee(referenceId, Constants.CONST_CARD_REISSUE)

       // Toast.makeText(context,"Enum:"+Constants.CONST_CARD_REISSUE,Toast.LENGTH_SHORT).show()
    }

    private fun setUiObserver() {

        accountViewModel.reissueCardMessage.observe(viewLifecycleOwner, { obj ->
            obj?.let { res ->
                // Toast.makeText(activity, res, Toast.LENGTH_LONG).show()

                showInfoDialog("yes", res, this)
            }
        })

        commonViewModel.serviceFailure.observe(viewLifecycleOwner, Observer { msg ->
            getServiceFee()

            Toast.makeText(context,"fail ",Toast.LENGTH_SHORT).show()
        })

        commonViewModel.serviceFeeResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { data ->
                if (data.data != null && data.data!!.requestedServiceFee != null && !TextUtils.isEmpty(data.data!!.requestedServiceFee)) {
                    setFees(data.data!!.requestedServiceFee.toDouble())

                    //Toast.makeText(context,"serviceFeesSuccess: "+ data.data!!.requestedServiceFee.toDouble(),Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun setFees(fee: Double) {

        binding.fee = fee

    }

    private fun setViews(fee: Double) {
       // binding.cardNumber = cardNum
        binding.address = "${MovoApp.db.getString(Constants.CONST_MY_SHIPPING_INFO)!!}"
        /*binding.sendTo = requestModel.toPhoneOrEmail
        binding.cardAmount = requestModel.amount
        binding.fee = fee
        binding.totalAmount = requestModel.amount + fee
        binding.notes = "NOTE: $${fee} will be deducted from card account $cardNum"*/

    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.btnconfirm.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> callFragment(R.id.mainContainer, MyCardsFragment(), null)
            R.id.rlRight -> {
               // Toast.makeText(context,"Address change",Toast.LENGTH_SHORT).show()
                ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                callFragment(R.id.mainContainer, ProfileFragment(), null)

            }
            R.id.btnconfirm -> {

                accountViewModel.reIssueCard(ReissueCardModelApi(primaryCardObj.referenceID!!))
            }
            R.id.btnCancel -> {
               // Toast.makeText(context,"Address change",Toast.LENGTH_SHORT).show()
                callFragment(R.id.mainContainer, MyCardsFragment(), null)

            }
        }
    }


    override fun onClickOk() {

        ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
        // callFragmentWithReplace(R.id.mainContainer, MyCardsFragment(), "SignInFragment")
        addFragment(R.id.mainContainer, MyCardsFragment(), null)

    }

    override fun onClickCancel() {

    }

    private fun checkAndSet() {
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
            //setCardData(1)
            primaryCardObj = mList1[0]
            setPrimaryCardData(0)
        }
    }

    private fun setPrimaryCardData(position: Int) {
        val cardNumber = "*" + mList1[position].cardNumber!!.substring(mList1[position].cardNumber!!.length - 4)
        val num = "${mList1[position].programAbbreviation}${cardNumber}"
        binding.cardNumber = num
        binding.cardAmount = mList1[position].balance
        binding.isCardSet = true
        referenceId = mList1[position].referenceID!!
        primaryAmount = mList1[position].balance

        //Toast.makeText(context, "Reference ID : "+referenceId, Toast.LENGTH_SHORT).show()

        getServiceFee()
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