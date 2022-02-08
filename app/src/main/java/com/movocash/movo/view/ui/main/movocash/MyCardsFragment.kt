package com.movocash.movo.view.ui.main.movocash

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment


import com.google.android.play.core.review.ReviewManagerFactory
import com.google.gson.Gson
import com.i2cinc.mcpsdk.MCPSDKManager
import com.i2cinc.mcpsdk.config.ScreenPresentingOption
import com.i2cinc.mcpsdk.config.UIConfig
import com.i2cinc.mcpsdk.interfaces.MCPSDKCallback
import com.i2cinc.mcpsdk.pushprovisioning.MCPPushProvisioningOperation
import com.i2cinc.mcpsdk.pushprovisioning.MCPPushProvisioningProvider
import com.i2cinc.mcpsdk.pushprovisioning.constant.PushProvisioningProviders
import com.i2cinc.mcpsdk.pushprovisioning.listener.MCPProviderStatusListener
import com.i2cinc.mcpsdk.pushprovisioning.listener.MCPTokenStatusListener
import com.i2cinc.mcpsdk.pushprovisioning.model.CardTokenStatus
import com.i2cinc.mcpsdk.pushprovisioning.model.PushProvisioningConfig
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.GetAuthTokenRequestModel
import com.movocash.movo.data.model.responsemodel.GetCardAccountsResponseModel
import com.movocash.movo.databinding.FragmentCardsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.adapter.AdapterMyCards
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.AccountViewModel
import com.movocash.movo.viewmodel.CardViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.system.exitProcess


class MyCardsFragment : BaseFragment(), View.OnClickListener, AdapterMyCards.ICardListener, SwipeRefreshLayout.OnRefreshListener, MCPSDKCallback {

    lateinit var binding: FragmentCardsBinding
    private lateinit var accountViewModel: AccountViewModel

    lateinit var operationITC: MCPPushProvisioningOperation
    lateinit var provider: MCPPushProvisioningProvider

    private lateinit var cardsViewModel: CardViewModel
    private var mList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var primaryList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var mList1: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var pList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var adapterList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var sortList: ArrayList<GetCardAccountsResponseModel.Card> = ArrayList()
    private var cardCVV = ""
    lateinit var adapter: AdapterMyCards
    var cardToken: String = ""
    lateinit var referenceID: String
    var isPreDone = true

    private var transitionFlag = 0
    private lateinit var programID: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cards, container, false)
        binding.tvTitle.isSelected = true
        binding.tvTitle.setText(
                "${MovoApp.db.getString(Constants.FIRST_NAME)} ${
                    MovoApp.db.getString(
                            Constants.LAST_NAME
                    )
                }"
        );
        cardsViewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
//
//        val config = PushProvisioningConfig()
//        config.client = TapAndPay.getClient(activity)
//        config.build()



//         var googleProvider = PushProvisioningProviders.GOOGLE.get().init(context, config)
//        providerStatusListener(googleProvider)

       // accountViewModel.getMyProfile(false)
        setUiObserver()
        setListeners()
        getCardsAccounts()
//        providerStatusListener(provider)
        //preLoadTask()



         if(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString()=="1")
      {
         // ActivityBase.activity.showToastMessage("Review Api Called")
          accountViewModel.ReviewAccount()


      }

        return binding.root
    }



    private fun getCardsAccounts() {
        try {
            val fragment = ActivityBase.activity.supportFragmentManager.findFragmentById(R.id.mainContainer)
            if (fragment is MyCardsFragment)
                cardsViewModel.getCardAccounts(!binding.srlCards.isRefreshing)
        } catch (ex: Exception) {
            cardsViewModel.getCardAccounts(!binding.srlCards.isRefreshing)
        }

       /* if(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString()!="1")
        {
            accountViewModel.ReviewAccount()
            ActivityBase.activity.showToastMessage("Review Api Called")

        }*/

    }

    private fun setUiObserver() {



        accountViewModel.ReviewAccountSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                // ActivityBase.activity.showToastMessage("working")

                if (obj.data!!.isEligibleForReview!!) {
                    //ActivityBase.activity.showToastMessage("eligible for review")
                    // ActivityBase.activity.showToastMessage("counter = "+obj.data!!.reviewCount)

                    //MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                    //accountViewModel.UPDATER_REVIEW_COUNT()

                    // ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())

                    if (obj.data!!.reviewCount == 0) {
                        //accountViewModel.UPDATER_REVIEW_COUNT()
                        //MovoApp.db.putString(Constants.REVIEW_COUNT_DB, "1")
                        // ActivityBase.activity.showToastMessage(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString())
                        activateReviewInfo()

                    } else {

                        //accountViewModel.UPDATER_REVIEW_COUNT()
                        //accountViewModel.logoutUser()

                    }
                } else {
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


        /////////////////////////////////////////////////
        cardsViewModel.cardAccountFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlCards.isRefreshing = false
           // ActivityBase.activity.showToastMessage(msg)
        })

        cardsViewModel.authFailure.observe(viewLifecycleOwner, Observer { msg ->
            //ActivityBase.activity.showToastMessage(msg)
        })
        cardsViewModel.authTokenFailure.observe(viewLifecycleOwner, Observer { msg ->
           // ActivityBase.activity.showToastMessage(msg)
        })
        cardsViewModel.authTokenResponse.observe(viewLifecycleOwner, Observer {
            it?.let { obj ->
                if (obj.data != null && obj.data!!.signOnToken != null && !TextUtils.isEmpty(obj.data!!.signOnToken))
                    cardToken = obj.data!!.signOnToken!!

                //ActivityBase.activity.showToastMessage("Token : "+obj.data!!.signOnToken!!)

                Log.i("token",obj.data!!.signOnToken!!)

                startTaskForCardUI()
            }

        })

        cardsViewModel.authResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            if (obj?.cardData != null && obj.cardData!!.cvV2 != null) {
                cardCVV = obj.cardData!!.cvV2!!
                if (mList.any { it.isPrimaryCardSpecified }) {
                    primaryList =
                            ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode != "F" })

                    val ind = mList.indexOf(mList.filter { it.isPrimaryCardSpecified }[0])
                    val ind1 =
                            primaryList.indexOf(primaryList.filter { it.isPrimaryCardSpecified }[0])
                    mList[ind1].cvv = cardCVV
                    adapter.notifyItemChanged(0)
                }
            }
        })

        cardsViewModel.cardAccountResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            obj?.let { res ->
                binding.srlCards.isRefreshing = false
                if (res.obj != null && res.obj!!.cards != null && res.obj!!.cards!!.size > 0) {
                    mList.clear()
                    mList.addAll(res.obj!!.cards!!)
                    setList()
                    res.obj!!.cards = mList
                    mList1 =
                            ArrayList(mList.filter { it.isPrimaryCardSpecified && it.statusCode != "F" })
                    MovoApp.db.putString(Constants.PRIMARY_REF_ID, mList1[0].referenceID!!)
                    var customerId = ""
                    if (mList1[0].customerId != null && !TextUtils.isEmpty(mList1[0].customerId) && mList1[0].customerId!!.length > 12) {
                        val last10 =
                                mList1[0].customerId!!.substring(mList1[0].customerId!!.length - 10)
                        val first3 = mList1[0].customerId!!.substring(0, 3)
                        customerId = "${first3}${last10}"
                    }

                    MovoApp.db.putString(Constants.CUSTOMER_ID, customerId)
                    MovoApp.db.putString(Constants.PRIMARY_REF_ID, mList1[0].referenceID!!)
                    saveCardsResponse(res)
                    // asdasfasfa
                }
            }
        })
    }

    private fun preLoadTask() {
        val params: HashMap<String, String> = HashMap()
        params["cardRefNo"] = referenceID
        params["authToken"] = cardToken
        MCPSDKManager.getInstance().preloadTaskList(params, this)
    }

    private fun setList() {
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
        referenceID = pList[0].referenceID!!
        setAdapter()
    }

    private fun startTaskForCardUI() {
        val config = UIConfig()
        config.presentingOption = ScreenPresentingOption.DIALOG
        config.backgroundColor = ContextCompat.getColor(ActivityBase.activity, R.color.red).toString()
        config.loadingIndicatorColor = ContextCompat.getColor(ActivityBase.activity, R.color.white).toString()
        MCPSDKManager.getInstance().setUiConfig(config)
        val params: HashMap<String, String> = HashMap()
        params["cardRefNo"] = referenceID
        params["authToken"] = cardToken
        MCPSDKManager.getInstance().startTask(Constants.MOVO_GET_CARD_INFO, params, this)
    }

    private fun saveCardsResponse(obj: GetCardAccountsResponseModel) {
        MovoApp.db.putString(Constants.CONST_CARD_RESPONSE, Gson().toJson(obj))
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvCards.layoutManager = manager

        adapterList = ArrayList(mList.filter { it.statusCode != "F" })
        //adapterList = mList1\\
        //sortList = ArrayList(adapterList.sortedBy { it.firstName?.toString() })

        adapter =AdapterMyCards(adapterList)
        //adapter = AdapterMyCards(adapterList)
        binding.rvCards.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
       // binding..setOnClickListener(this)
        binding.srlCards.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> {

                openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
                /*  if(MovoApp.db.getString(Constants.REVIEW_COUNT_DB).toString()!="1")
                {
                    accountViewModel.ReviewAccount()
                    ActivityBase.activity.showToastMessage("Review Api Called")

                }*/
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Toast.makeText(context, "back pressed", Toast.LENGTH_SHORT).show()


                exitProcess(0)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onClickCard(position: Int) {
        val cardNumber = "*" + adapterList[position].cardNumber!!.substring(adapterList[position].cardNumber!!.length - 4)
        val num = "${adapterList[position].programAbbreviation}${cardNumber}"
        addFragment(
                R.id.mainContainer, CardTransactionsFragment.newInstance(
                adapterList[position].referenceID!!,
                num,
                adapterList[position].balance,
                position == 0
        ), "CardTransactionsFragment"
        )
    }

    override fun onClickViewCard() {
        cardsViewModel.getAuthToken(GetAuthTokenRequestModel(referenceID, ""))





    }




    override fun onRefresh() {
        binding.srlCards.isRefreshing = true
        getCardsAccounts()
    }

    override fun onLoadingStarted(): Boolean {
        return true
    }

    override fun onLoadingCompleted() {
    }

    override fun onSuccess(p0: MutableMap<String, String>?) {
    }

    override fun onError(p0: String?, p1: String?) {
    }

    override fun onCancel() {
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

/*    private fun providerStatusListener(provider: MCPPushProvisioningProvider): MCPProviderStatusListener? {
        //ActivityBase.activity.showToastMessage("providerStatusListen Activated")
        return object : MCPProviderStatusListener {
            override fun onNotSupported(operation: MCPPushProvisioningOperation) {
               // ActivityBase.activity.showToastMessage("onNotSupported")
            }
            override fun onNotConfigured(provider: MCPPushProvisioningProvider) {
               // ActivityBase.activity.showToastMessage("onNotConfigured")
            }
            override fun onSupported(operation: MCPPushProvisioningOperation) {

               // ActivityBase.activity.showToastMessage("OnSupported")
                operationITC = operation
                *//*operation.getCardsTokenStatus(adapterList[position].referenceID!!, additionalList, authToken, object : MCPTokenStatusListener {
                    override fun onSuccess(cardTokenStatuses: List<CardTokenStatus>) {}
                    override fun onFailure(errorCode: String, description: String) {}
                })*//*
            }
            override fun onNotAllowed(mcpPushProvisioningProvider: MCPPushProvisioningProvider) {
               // ActivityBase.activity.showToastMessage("onNotAllowed")
            }
            override fun onFailure(errorCode: String, description: String) {
               // ActivityBase.activity.showToastMessage("onFailure")
            }
        }
    }*/

    override fun googlePayImplementation(position: Int) {

       // ActivityBase.activity.showToastMessage("Google Play Implementation")
      /*  operationITC.getCardsTokenStatus(adapterList[position].referenceID!!,
                null,
                MovoApp.db.getString(
                        Constants.ACCESS_TOKEN
                ),
                object : MCPTokenStatusListener {
                    override fun onSuccess(cardTokenStatuses: List<CardTokenStatus>) {
                        //ActivityBase.activity.showToastMessage("success")
                    }

                    override fun onFailure(errorCode: String, description: String) {

                       // ActivityBase.activity.showToastMessage("success")
                    }
                })*/
    }

}