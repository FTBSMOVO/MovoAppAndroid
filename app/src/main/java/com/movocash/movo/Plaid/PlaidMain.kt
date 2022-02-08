package com.movocash.movo.Plaid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.movocash.movo.R
import com.movocash.movo.data.model.requestmodel.PlaidCreateBankAccountRequestModel
import com.movocash.movo.utilities.Constants.BANK_TO_CARD_TRANSFER
import com.movocash.movo.utilities.extensions.showInfoDialog
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.about.AboutUsFragment
import com.movocash.movo.viewmodel.BankViewModel
import com.plaid.link.Plaid
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkResultHandler
import com.plaid.linksample.network.LinkSampleApi
import com.plaid.linksample.network.LinkTokenRequester
import com.plaid.linksample.network.UpdatedGeneralResponseModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


import androidx.fragment.app.FragmentTransaction
import com.movocash.movo.view.ui.auth.AuthActivity
import com.movocash.movo.view.ui.main.MainActivity
import com.movocash.movo.view.ui.main.digitalbanking.mybankaccounts.MyBankAccountFragment
import com.movocash.movo.view.ui.main.digitalbanking.scheduletransfer.ScheduleTransferFragment
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment


class PlaidMain : ActivityBase() {
    private lateinit var result: TextView
    private lateinit var tokenResult: TextView
    private lateinit var bankViewModel: BankViewModel

    private lateinit var publicToken: String

    private val myPlaidResultHandler by lazy {
        LinkResultHandler(
            onSuccess = {
                tokenResult.text = getString(R.string.public_token_result, it.publicToken)
                //ActivityBase.activity.showToastMessage("PermanentToken : "+it.publicToken)
                result.text = "success"
                bankViewModel.plaidcreateBankAccount(PlaidCreateBankAccountRequestModel(it.publicToken,BANK_TO_CARD_TRANSFER))
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
        setContentView(R.layout.activity_plaid_main)
        result = findViewById(R.id.result)
        tokenResult = findViewById(R.id.public_token_result)
        bankViewModel = ViewModelProvider(this).get(BankViewModel::class.java)
        setUIObserver()

        bankViewModel.getPlaidToken(true)
        /*val button = findViewById<View>(R.id.open_link)
        button.setOnClickListener {
            setOptionalEventListener()
           // openLink()

            bankViewModel.getPlaidToken(true)

            //getCurrentData()
        }*/


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
                   // ActivityBase.activity.showToastMessage("Account Created successCase: "+obj.messages.toString())

                    /*al fragment: Fragment = TestFragment()
                    val fragmentManager = supportFragmentManager
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()*/

                    //ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    //callFragment(R.id.MainContainer, MyBankAccountFragment(), null)

                    callFragment(R.id.mainContainer, MyCardsFragment(), null)

                   /* ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
                    val intent = Intent(ActivityBase.activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    ActivityBase.activity.startActivity(intent)*/


                }
            }
        })
    }

    /**
     * Optional, set an [event listener](https://plaid.com/docs/link/android/#handling-onevent).
     */
    private fun setOptionalEventListener() = Plaid.setLinkEventListener { event ->
        Log.i("Event", event.toString())
    }

    fun getCurrentData() {
        Toast.makeText(baseContext, "Function Called", Toast.LENGTH_SHORT).show()
        val okHttpClient: OkHttpClient =  OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://devappapi.movo.cash/api/v4/")

            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()
        val service = retrofit.create(LinkSampleApi::class.java)
        val call = service.getPlaidToken()
        call.enqueue(object : Callback<UpdatedGeneralResponseModel> {
            override fun onResponse(call: Call<UpdatedGeneralResponseModel>, response: Response<UpdatedGeneralResponseModel>) {
                if (response.code() == 200) {

                    val response = response.body()!!

                    Toast.makeText(baseContext, "Token : "+response.data, Toast.LENGTH_SHORT).show()

                   // myTokenSuccess(response.data!!)

                }
                if (response.code() == 404) {

                    //val response = response.body()!!

                    Toast.makeText(baseContext, "Token : 404", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<UpdatedGeneralResponseModel>, t: Throwable) {

                Toast.makeText(baseContext, "Api call Failed!"+t.message, Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> =
                trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager


            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    /**
     * For all Link configuration options, have a look at the
     * [parameter reference](https://plaid.com/docs/link/android/#parameter-reference).
     */
    private fun openLink() {
        LinkTokenRequester.token.subscribe(::onLinkTokenSuccess, ::onLinkTokenError)
    }

    private fun onLinkTokenSuccess(linkToken: String) {
        val tokenConfiguration = LinkTokenConfiguration.Builder()
            .token(linkToken)
            .build()
        Plaid.create(
            this.application,
            tokenConfiguration
        ).open(this)
    }


    private fun myTokenSuccess(linkToken: String) {
        val tokenConfiguration = LinkTokenConfiguration.Builder()
            .token(linkToken)
            .build()
        Plaid.create(
            this.application,
            tokenConfiguration
        ).open(this)
    }

    private fun onLinkTokenError(error: Throwable) {
        if (error is java.net.ConnectException) {
            Toast.makeText(this, "Please run `sh start_server.sh <client_id> <sandbox_secret>`", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (!myPlaidResultHandler.onActivityResult(requestCode, resultCode, data)) {
            Log.i(PlaidMain::class.java.simpleName, "Not handled")

           // ActivityBase.activity.supportFragmentManager.popBackStackImmediate()
          //  callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
        }


       // callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
        //callFragment(R.id.mainContainer, ScheduleTransferFragment(), null)
    }


}