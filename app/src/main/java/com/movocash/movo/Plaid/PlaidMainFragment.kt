package com.movocash.movo.Plaid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.easypeasyapp.easypeasy.view.ui.base.BaseFragment
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.PlaidCreateBankAccountRequestModel
import com.movocash.movo.databinding.FragmentPlaidMainBinding
import com.movocash.movo.databinding.FragmentSideMenuBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.about.AboutUsFragment
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.MyBankAccountFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.plaid.link.Plaid
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResultHandler

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaidMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaidMainFragment : BaseFragment(), View.OnClickListener {


    private lateinit var result: TextView
    private lateinit var tokenResult: TextView
    private lateinit var bankViewModel: BankViewModel
    lateinit var binding: FragmentPlaidMainBinding
    private lateinit var publicToken: String

    private val myPlaidResultHandler by lazy {
        LinkResultHandler(
            onSuccess = {
                tokenResult.text = getString(R.string.public_token_result, it.publicToken)
                //ActivityBase.activity.showToastMessage("PermanentToken : "+it.publicToken)
                result.text = "success"
                bankViewModel.plaidcreateBankAccount(
                    PlaidCreateBankAccountRequestModel(it.publicToken,
                        Constants.BANK_TO_CARD_TRANSFER
                    )
                )
            },
            onExit = {
                tokenResult.text = ""
                if (it.error != null) {
                    result.text = "Exit"
                    // callFragment(R.id.plaidMainContainer, MyBankAccountFragment(), null)

                    /*val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    ActivityBase.activity.startActivity(intent)*/

                } else {
                    result.text = "Cancel"
                    // callFragment(R.id.plaidMainContainer, MyBankAccountFragment(), null)

                    /*val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    ActivityBase.activity.startActivity(intent)*/

                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plaid_main, container, false)
        result = binding.result
        tokenResult = binding.publicTokenResult
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        setUIObserver()

        bankViewModel.getPlaidToken(true)

        return binding.root


    }

    private fun setUIObserver() {
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
                    ActivityBase.activity.showToastMessage("Account Created successCase: "+obj.messages.toString())

                    /*al fragment: Fragment = TestFragment()
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()*/

                    //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    addFragment(R.id.mainContainer, MyBankAccountFragment(), null)

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

    companion object {


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlUserPic -> {

            }
        }
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