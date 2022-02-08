package com.movocash.movo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.work.WorkManager.initialize
import androidx.work.impl.WorkManagerImpl.initialize
import com.acuant.acuantcommon.initializer.AcuantInitializer.Companion.initialize
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.gms.flags.FlagRegistry.initialize
/*import com.github.tamir7.contacts.Contacts*/
import com.i2cinc.mcpsdk.MCPSDKManager
import com.movocash.movo.data.remote.ApiClient
import com.movocash.movo.data.remote.ApiClient2
import com.movocash.movo.data.remote.ApiInterface
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.helper.TinyDB

import com.threatmetrix.TrustDefender.TMXConfig
import com.threatmetrix.TrustDefender.TMXEndNotifier
import com.threatmetrix.TrustDefender.TMXProfiling
import com.threatmetrix.TrustDefender.TMXProfilingConnections.TMXProfilingConnections
import com.threatmetrix.TrustDefender.TMXProfilingHandle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class MovoApp : MultiDexApplication() {

    private lateinit var appSharedPrefs: SharedPreferences
    private lateinit var prefsEditor: SharedPreferences.Editor
    private var SHARED_NAME = "com.app.movoapp"

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        initPreferences()
      //  ContactsContract.Contacts.initialize(this)
//        FirebaseApp.initializeApp(context)
        configRetrofit()
        getFcmTokken()
        initThreatMatrix()
    }

    private fun initPreferences() {
        this.appSharedPrefs = getSharedPreferences(SHARED_NAME, Activity.MODE_PRIVATE)
        this.prefsEditor = appSharedPrefs.edit()
        db = TinyDB(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()

        Log.i(TAG, "Freeing memory ...")

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.v(TAG, "public void onTrimMemory (int level)")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }


    companion object {
        lateinit var db: TinyDB

        @SuppressLint("StaticFieldLeak")
        private var instance: MovoApp? = null
        private var ORG_ID: String = "316ivgf0"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private val TAG = MovoApp::class.java.name
        lateinit var apiService: ApiInterface
        lateinit var apiService2: ApiInterface
        private var parentJob = Job()
        private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.Main
        val scope = CoroutineScope(coroutineContext)

        fun configRetrofit() {
            apiService = ApiClient.getClient()!!.create(ApiInterface::class.java)
            apiService2 = ApiClient2.getClient()!!.create(ApiInterface::class.java)
        }

        fun initThreatMatrix() {
            val profilingConnections = TMXProfilingConnections().setConnectionTimeout(20, TimeUnit.SECONDS).setRetryTimes(3)
            val config = TMXConfig().setOrgId(ORG_ID)
                    .setContext(context).setProfilingConnections(profilingConnections)
                    .setRegisterForLocationServices(true);

            TMXProfiling.getInstance().init(config)
            Log.d("Check", "Successfully init-ed ")

            /*val profilingHandle = TMXProfiling.getInstance().profile { result ->
                db.putString(Constants.SESSION_ID, result.sessionID)
                Log.e("SESSION_STATUS", "${result.status}")
                Log.e("SESSION_ID", "${result.sessionID}")
            }*/

            val profilingHandle = TMXProfiling.getInstance().profile(object : TMXEndNotifier {
                override fun complete(result: TMXProfilingHandle.Result?) {
                    db.putString(Constants.SESSION_ID, result!!.sessionID)
                    db.putString(Constants.SESSION_ID_STATUS, result.status!!.toString())

                    Log.e("SESSION_STATUS", "${result!!.status}")
                    Log.e("SESSION_ID", "${result!!.sessionID}")
                }

            })
        }
    }

    private fun getFcmTokken() {
//        FirebaseInstanceId.getInstance().instanceId
//                .addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w(TAG, "getInstanceId failed", task.exception)
//                        return@OnCompleteListener
//                    }
//                    val tokken = task.result?.token
//                    HooleyApp.db.putString(Constants.DEVICE_ID, tokken!!)
//                    Log.d("Token", "New Token : $tokken")
//
//                })

    }
}