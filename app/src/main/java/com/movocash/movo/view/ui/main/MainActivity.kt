package com.movocash.movo.view.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.acuant.acuantcamera.initializer.MrzCameraInitializer
import com.acuant.acuantcommon.exception.AcuantException
import com.acuant.acuantcommon.initializer.AcuantInitializer
import com.acuant.acuantimagepreparation.initializer.ImageProcessorInitializer
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.i2cinc.mcpsdk.MCPSDKManager
import com.i2cinc.mcpsdk.config.ScreenPresentingOption
import com.i2cinc.mcpsdk.config.UIConfig
import com.movocash.movo.MovoApp
import com.movocash.movo.MovoApp.Companion.context
import com.movocash.movo.R
import com.movocash.movo.databinding.ActivityMainBinding
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.view.ui.auth.AuthActivity
import com.movocash.movo.view.ui.base.ActivityBase
import com.movocash.movo.view.ui.main.movocash.MyCardsFragment
import com.movocash.movo.view.ui.main.movopay.sendmoney.SendMoneyFragment
import com.movocash.movo.view.ui.main.others.SideMenuFragment
import com.movocash.movo.view.ui.main.profile.ProfileFragment
import com.movocash.movo.view.ui.main.profile.VerifyProfileCodeFragment
import com.movocash.movo.viewmodel.AccountViewModel

class MainActivity : ActivityBase() {

    private lateinit var accountViewModel: AccountViewModel
    var handler: Handler? = null
    var r: Runnable? = null
   // val reviewInfo:ReviewInfo


   // val manager = ReviewManagerFactory.create(context)
   // private lateinit var manager: ReviewManagerFactory

    companion object {
        lateinit var binding: ActivityMainBinding
        lateinit var mActivity: AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        MCPSDKManager.getInstance().init(ActivityBase.activity, Constants.MOVO_APP_ID, Constants.MOVO_API_KEY)
        setStatusBarColor(ContextCompat.getColor(this, R.color.white), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        setUiObserver()
        mActivity = this
        callFragment(R.id.mainContainer, MyCardsFragment(), null)
        checkSessionExpiration()

        /* try {

             AcuantInitializer.initialize(
                 "acuant.config.xml",
                 this@MainActivity,
                 listOf(ImageProcessorInitializer(), EchipInitializer(), MrzCameraInitializer()),initCallback
             )
         } catch (e: AcuantException) {
             Log.e("Acuant Error", e.toString())
         }*/
    }

    private fun checkSessionExpiration() {
        handler = Handler()
        r = Runnable {
            ActivityBase.activity.showToastMessage("")
            accountViewModel.logoutUser()
        }
        startHandler()
    }

    private fun setUiObserver() {
        accountViewModel.logoutFailure.observe(this, Observer {
            activity.showToastMessage(it)
        })

        accountViewModel.logoutSuccess.observe(this, Observer {
            toTheAuth()
        })
    }

    private fun toTheAuth() {
        val lastUserName = MovoApp.db.getString(Constants.USER_NAME)
        val lastUserPass = MovoApp.db.getString(Constants.USER_PASS)
        val rememberUserPass = MovoApp.db.getString(Constants.REMEMBERED_USER_NAME)
        MovoApp.db.clear()
        MovoApp.db.putString(Constants.USER_NAME, lastUserName!!)
        MovoApp.db.putString(Constants.USER_PASS, lastUserPass!!)
        MovoApp.db.putString(Constants.REMEMBERED_USER_NAME, rememberUserPass!!)
        MovoApp.initThreatMatrix()
        val intent = Intent(activity, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.startActivity(intent)
        activity.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)) {
            is SendMoneyFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
            is ProfileFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
            is SideMenuFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }
            is VerifyProfileCodeFragment -> {
                run {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }
            }

        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        stopHandler() //stop first and then start
        startHandler()
    }

    private fun stopHandler() {
        try {
            handler!!.removeCallbacks(r!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun startHandler() {
        try {
            handler!!.postDelayed(r!!, 5 * 60 * 1000.toLong()) //for 5 minutes
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public interface IAcuantPackageCallback{
        fun onInitializeSuccess()

        fun onInitializeFailed(error: List<Error>)
    }



    val initCallback = object: com.acuant.acuantcommon.initializer.IAcuantPackageCallback {

        override fun onInitializeSuccess() {

            //Toast.makeText(getApplicationContext(),"SDK INITIALIZED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

        }

        override fun onInitializeFailed(error: List<com.acuant.acuantcommon.model.Error>) {

            Toast.makeText(getApplicationContext(),"SDK NOT INITIALIZED", Toast.LENGTH_SHORT).show();

        }
    }

 /*   private fun activateReviewInfo()
    {
            val manager = ReviewManagerFactory.create(context)


        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                 val reviewInfo = task.result

                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.

                    Toast.makeText(context,"Review is completed",Toast.LENGTH_SHORT).show()
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }



    }
*/
    private fun StartReviewFlow()
    {

    }
}