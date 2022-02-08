package com.movocash.movo.view.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.acuant.acuantcamera.initializer.MrzCameraInitializer
import com.acuant.acuantcommon.exception.AcuantException
import com.acuant.acuantcommon.initializer.AcuantInitializer
import com.acuant.acuantimagepreparation.initializer.ImageProcessorInitializer
import com.google.android.play.core.review.ReviewManagerFactory
import com.movocash.movo.MovoApp
import com.movocash.movo.R
import com.movocash.movo.databinding.ActivitySplashBinding
import com.movocash.movo.view.ui.auth.AuthActivity
import com.movocash.movo.view.ui.base.ActivityBase

class SplashActivity : ActivityBase() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setStatusBarColor(ContextCompat.getColor(this, R.color.white), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        gotoWelcome()
       // activateReviewInfo()

        try {

            AcuantInitializer.initialize(
                "acuant.config.xml",
                this,
                listOf(ImageProcessorInitializer(), /*EchipInitializer(),*/ MrzCameraInitializer()),initCallback
            )
        } catch (e: AcuantException) {
            Log.e("Acuant Error", e.toString())
        }
    }

    private fun gotoWelcome() {
        Handler().postDelayed({
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            this.finish()
        }, 3000)
    }

    val initCallback = object: com.acuant.acuantcommon.initializer.IAcuantPackageCallback {

        override fun onInitializeSuccess() {

           // Toast.makeText(getApplicationContext(),"SDK INITIALIZED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

        }

        override fun onInitializeFailed(error: List<com.acuant.acuantcommon.model.Error>) {

            Toast.makeText(getApplicationContext(),"SDK NOT INITIALIZED", Toast.LENGTH_SHORT).show();

        }
    }

   /* private fun activateReviewInfo()
    {
        val manager = ReviewManagerFactory.create(MovoApp.context)


        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result

                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { task ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                    if (task.isSuccessful) {

                        Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else
                        Toast.makeText(MovoApp.context, "Review is completed", Toast.LENGTH_SHORT)
                            .show()
                }

            } else {
                // There was some problem, log or handle the error code.
                //@ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }



    }*/
}