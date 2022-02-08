package com.movocash.movo.view.ui.main.echeckbook.schedulepayments

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
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CustomPaymentDetailModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.data.model.responsemodel.GetPaymentHistoryResponseModel
import com.movocash.movo.databinding.FragmentScheduledPaymentsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.ICardClickListener
import com.movocash.movo.utilities.extensions.showAccountSelectionDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.echeckbook.adapter.AdapterPaymentHistory
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CardViewModel
import com.movocash.movo.viewmodel.ECheckBookViewModel
import java.lang.reflect.Type

class SchedulePaymentsFragment : BaseFragment(), View.OnClickListener, AdapterPaymentHistory.IPayHistory, SwipeRefreshLayout.OnRefreshListener, ICardClickListener {

    lateinit var binding: FragmentScheduledPaymentsBinding
    private var mList = ArrayList<CustomPaymentDetailModel>()
    private lateinit var accountViewModel: AccountViewModel

    private var mainList = ArrayList<GetPaymentHistoryResponseModel.Transaction>()
    private lateinit var viewModel: ECheckBookViewModel
    private var sendFromAccount = ""
    lateinit var adapter: AdapterPaymentHistory

    private var cardList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var referenceId = ""
    private lateinit var cardsViewModel: CardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scheduled_payments, container, false)
        viewModel = ViewModelProvider(this).get(ECheckBookViewModel::class.java)
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        setUiObserver()
        checkAndSetCards()
        checkAndSet()
        setViews()
        setListeners()
        if(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString()=="1")
        {
            //ActivityBase.activity.showToastMessage("Review Api Called")
            accountViewModel.ReviewAccount()


        }
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



        accountViewModel.ReviewAccountSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                // ActivityBase.activity.showToastMessage("working")

                if (obj.data!!.isEligibleForReview!!)
                {
                   // ActivityBase.activity.showToastMessage("eligible for review")
                   // ActivityBase.activity.showToastMessage("counter = "+obj.data!!.reviewCount)

                    //MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                    //accountViewModel.UPDATER_REVIEW_COUNT()

                    // ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())

                    if(obj.data!!.reviewCount == 0 )
                    {
                        //accountViewModel.UPDATER_REVIEW_COUNT()
                        //MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                        // ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())
                        activateReviewInfo()

                    }
                    else
                    {

                        //accountViewModel.UPDATER_REVIEW_COUNT()
                        //accountViewModel.logoutUser()

                    }
                }
                else
                {
                    //accountViewModel.logoutUser()
                }
            }
        })

        accountViewModel.ReviewAccountFailure.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                //ActivityBase.activity.showToastMessage(obj)
                //accountViewModel.UPDATER_REVIEW_COUNT()

            }
        })

        accountViewModel.UPDATER_REVIEW_COUNT_Success.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                // ActivityBase.activity.showToastMessage(obj)


            }
        })

        accountViewModel.UPDATER_REVIEW_COUNT_Failure.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                // ActivityBase.activity.showToastMessage(obj)


            }
        })




        //////////////////////////////////////////////////////
        viewModel.paymentHistoryFailure.observe(viewLifecycleOwner, Observer {
            binding.srlHistory.isRefreshing = false
            ActivityBase.activity.showToastMessage(it)
        })

        viewModel.paymentHistoryResponseModel.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                binding.srlHistory.isRefreshing = false

                if (obj.data != null && obj.data!!.transactions != null && obj.data!!.transactions!!.size > 0) {
                    if (obj.data!!.transactions!!.any { it.status == Constants.PAYMENT_IN_PROGRESS || it.status == Constants.PAYMENT_LOGGED || it.status == Constants.PAY_SCHEDULED }) {
                        mList.clear()
                        mainList.clear()
                        mainList.addAll(obj.data!!.transactions!!)
                        obj.data!!.transactions!!.filter { it.status == Constants.PAYMENT_IN_PROGRESS || it.status == Constants.PAYMENT_LOGGED || it.status == Constants.PAY_SCHEDULED }
                                .mapTo(mList) {
                                    CustomPaymentDetailModel(sendFromAccount, it.payeeName!!, it.scheduledDate!!, it.amount, 0.0, 0.0, it.status!!, "", "${it.payeeCity}, ${it.payeeState}", Constants.CONST_TEXT, it.billPaymentTransId!!, it.payeeAccountNumber!!)
                                }

                   /* obj.data!!.transactions!!.filter { it.status == Constants.PAYMENT_IN_PROGRESS || it.status == Constants.PAYMENT_LOGGED || it.status == Constants.PAYMENT_SCHEDULED }
                            .mapTo(mList) {
                                CustomPaymentDetailModel(sendFromAccount, it.payeeName!!, it.scheduledDate!!, it.amount, 0.0, 0.0, it.status!!, "", "${it.payeeCity}, ${it.payeeState}", Constants.CONST_TEXT, it.billPaymentTransId!!, it.payeeAccountNumber!!)
                            }*/
                        binding.isData = true
                        setAdapter()
                      //  ActivityBase.activity.showToastMessage("Adapter set ho gya")

                        setDate()
                    } else {
                        binding.isData = false
                        //ActivityBase.activity.showToastMessage("koi transaction nai mili")
                    }

                } else {
                    binding.isData = false
                  // ActivityBase.activity.showToastMessage("main list empty hai")
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
        val reqObj = MakeUpdatePaymentRequestModel(referenceId,obj2.billPaymentTransId!!, obj2.payeeSerialNo!!, obj2.payeeAccountNumber!!, obj2.amount, obj2.scheduledDate!!, obj2.description!!)
        addFragment(R.id.mainContainer, SchedulePaymentDetailFragment.newInstance(obj, reqObj), "PaymentHistoryDetailFragment")
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

    private fun activateReviewInfo()
    {
        val manager = ReviewManagerFactory.create(MovoApp.context)


        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(ActivityBase.activity, reviewInfo)
                flow.addOnCompleteListener { task ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    if (task.isSuccessful) {

                        /*Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                                .show()*/

                        accountViewModel.UPDATER_REVIEW_COUNT()
                        // accountViewModel.logoutUser()
                    }
                    else
                    {
                        // accountViewModel.logoutUser()
                    }
                    /*Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()*/
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                // accountViewModel.logoutUser()
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