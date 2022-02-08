package com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.data.model.responsemodel.GetBankAccountsResponseModel
import com.movocash.movo.databinding.FragmentMyAccountsBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.adapter.AdapterMyBankAccounts
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.google.gson.Gson
import com.movocash.movo.Plaid.PlaidMain
import com.movocash.movo.data.model.requestmodel.PlaidCreateBankAccountRequestModel
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.plaid.link.Plaid
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResultHandler

class MyBankAccountFragment : BaseFragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterMyBankAccounts.IBankAccounts {

    lateinit var binding: FragmentMyAccountsBinding
    private var mList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()
    private var plaidList: ArrayList<GetBankAccountsResponseModel.Account> = ArrayList()
    private lateinit var bankViewModel: BankViewModel

    private lateinit var publicToken: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_accounts, container, false)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        setUIObserver()
        getAllBankAccounts()
        setViews()
        setListeners()



        return binding.root
    }

    private fun getAllBankAccounts() {
        bankViewModel.getBankAccounts(!binding.srlBanks.isRefreshing)
    }

    private fun setUIObserver() {
        bankViewModel.getBankAccountsFailure.observe(viewLifecycleOwner, Observer { msg ->
            binding.srlBanks.isRefreshing = false
            ActivityBase.activity.showToastMessage(msg)
        })

        bankViewModel.getBankAccountsResponseModel.observe(viewLifecycleOwner, Observer { obj ->
            binding.srlBanks.isRefreshing = false
            if (obj != null && obj.data != null && obj.data!!.accounts != null && obj.data!!.accounts!!.size > 0) {
                MovoApp.db.putString(Constants.CONST_MY_BANKS_RESPONSE, Gson().toJson(obj))
                binding.isData = true
                mList.clear()
                mList.addAll(obj.data!!.accounts!!)
                plaidList = mList.filter { it.achType==1 } as ArrayList<GetBankAccountsResponseModel.Account>
                setAdapter()
            } else
                binding.isData = false
        })

        ////////////////////////////
        bankViewModel.getPlaidTokenFailure.observe(this, Observer { msg ->
            ActivityBase.activity.showToastMessage(msg)


        })

        bankViewModel.getPlaidTokenSuccess.observe(this, Observer {
            it?.let { obj ->
                if (obj != null ) {
                    // ActivityBase.activity.showToastMessage("Token : "+ obj.data.toString())

                    myTokenSuccess(obj.data!!.toString())

                    publicToken = obj.data!!.toString()

                    // bankViewModel.plaidcreateBankAccount(PlaidCreateBankAccountRequestModel(publicToken,BANK_TO_CARD_TRANSFER))
                }
            }
        })

        bankViewModel.plaidcreateBankAccountFailure.observe(this, Observer { msg ->
            ActivityBase.activity.showToastMessage("Failure : "+msg)


        })

        bankViewModel.plaidcreateBankAccountResponseModel.observe(this, Observer {
            it?.let { obj ->
                if (obj != null ) {
                    //ActivityBase.activity.showToastMessage("Account Created successCase: "+obj.messages.toString())

                    /*al fragment: Fragment = TestFragment()
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()*/

                    //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    // addFragment(R.id.mainContainer, MyBankAccountFragment(), null)

                    //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    // addFragment(R.id.mainContainer, MyBankAccountFragment(), null)

                    /* ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                     val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                     ActivityBase.activity.startActivity(intent)*/


                }
            }
        })
    }

    private fun setViews() {
        binding.tvTitle.isSelected = true
        binding.isData = true
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(ActivityBase.activity)
        binding.rvBankAccounts.layoutManager = manager
        // val adapter = AdapterMyBankAccounts(mList)
        val adapter = AdapterMyBankAccounts(plaidList)
        binding.rvBankAccounts.adapter = adapter
        adapter.setMyListener(this)
    }

    private fun setListeners() {
        binding.rlLeft.setOnClickListener(this)
        binding.rlRight.setOnClickListener(this)
        binding.srlBanks.setOnRefreshListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlLeft -> openSideMenu(R.id.mainContainer, SideMenuFragment(), "SideMenuFragment")
            R.id.rlRight -> {
                // callFragmentWithReplace(R.id.mainContainer, SelectBankTypeFragment(), "SelectBankTypeFragment")
                bankViewModel.getPlaidToken(true)
            }
        }
    }


    override fun onRefresh() {
        getAllBankAccounts()
    }

    override fun onItemClicked(position: Int) {
        val obj = mList[position]
        val requestObj = CreateBankAccountRequestModel(obj.accountSrNo!!, obj.accountType, obj.accountTitle!!, obj.bankName!!, obj.routingNumber!!, obj.accountTypeSpecified, obj.accountNumber!!, obj.accountNickname!!, obj.comments!!)
        addFragment(R.id.mainContainer, AddBankAccountConfirmationFragment.newInstance(requestObj), "AddBankAccountConfirmationFragment")
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

    ////////////////////////////// Plaid Implementation  ////////////////////////

    private val myPlaidResultHandler by lazy {
        LinkResultHandler(
            onSuccess = {
                //tokenResult.text = getString(R.string.public_token_result, it.publicToken)
                //ActivityBase.activity.showToastMessage("PermanentToken : "+it.publicToken)
                // result.text = "success"
                bankViewModel.plaidcreateBankAccount(
                    PlaidCreateBankAccountRequestModel(it.publicToken,
                        Constants.BANK_TO_CARD_TRANSFER
                    )
                )
            },
            onExit = {
                //tokenResult.text = ""
                if (it.error != null) {
                    // result.text = "Exit"
                    // callFragment(R.id.plaidMainContainer, MyBankAccountFragment(), null)

                    /*val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    ActivityBase.activity.startActivity(intent)*/

                } else {
                    //result.text = "Cancel"
                    // callFragment(R.id.plaidMainContainer, MyBankAccountFragment(), null)

                    /*val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    ActivityBase.activity.startActivity(intent)*/

                }
            }
        )
    }

    private fun onLinkTokenSuccess(linkToken: String) {
        val tokenConfiguration = LinkTokenConfiguration.Builder()
            .token(linkToken)
            .build()
        Plaid.create(requireActivity()!!.application!!, tokenConfiguration).open(this)
    }


    private fun myTokenSuccess(linkToken: String) {
        val tokenConfiguration = LinkTokenConfiguration.Builder()
            .token(linkToken)
            .build()
        Plaid.create(
            requireActivity().application,
            tokenConfiguration
        ).open(this)
    }

    private fun onLinkTokenError(error: Throwable) {
        if (error is java.net.ConnectException) {
            Toast.makeText(context, "Please run `sh start_server.sh <client_id> <sandbox_secret>`", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
            Log.i(PlaidMain::class.java.simpleName, "Not handled")

            // ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
            //  callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
        }


        // callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
        //callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
    }
}