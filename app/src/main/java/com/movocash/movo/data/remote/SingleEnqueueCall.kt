package com.movocash.movo.data.remote

import com.movocash.movo.data.model.responsemodel.GeneralResponseModel
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.hideAppLoader
import com.movocash.movo.utilities.extensions.showAppLoader
import com.movocash.movo.view.ui.base.ActivityBase
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.movocash.movo.utilities.extensions.onTokenExpiredLogout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException


object SingleEnqueueCall {

    var snackbar = Snackbar.make(ActivityBase.activity.findViewById(android.R.id.content), Constants.CONST_NO_INTERNET_CONNECTION, Snackbar.LENGTH_INDEFINITE)

    fun <T> callRetrofit(call: Call<T>, strApiName: String, isLoaderShown: Boolean, apiListener: IGenericCallBack) {
        if (isLoaderShown)
            ActivityBase.activity.showAppLoader()
        snackbar.dismiss()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                hideAppLoader()
                if (response.isSuccessful) {
                    apiListener.success(strApiName, response.body())
                } else {
                    when {
                        response.code() == 401 -> {
                            if (strApiName != Constants.LOGOUT_URL)
                                onTokenExpiredLogout()
                            return
                        }
                        response.errorBody() != null -> try {
                            val gson = GsonBuilder().create()
                            try {
                                val errorModel: GeneralResponseModel = gson.fromJson(response.errorBody()!!.string(), GeneralResponseModel::class.java)
                                apiListener.failure(strApiName, errorModel.messages, response.body())
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                apiListener.failure(strApiName, Constants.CONST_SERVER_NOT_RESPONDING, response.body())
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            apiListener.failure(strApiName, Constants.CONST_SERVER_NOT_RESPONDING, response.body())
                        }
                        else -> {
                            apiListener.failure(strApiName, Constants.CONST_SERVER_NOT_RESPONDING, response.body())
                            return
                        }
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                hideAppLoader()
                val callBack = this
                if (t.message != "Canceled") {
                    if (t is UnknownHostException || t is IOException) {
                            snackbar.setAction("Retry") {
                                snackbar.dismiss()
                                enqueueWithRetry(call, callBack, isLoaderShown)
                            }
                            snackbar.show()
                        //apiListener.failure(strApiName, Constants.CONST_NO_INTERNET_CONNECTION)
                    }

                    else {
                        apiListener.failure(strApiName, t.toString())
                    }
                }
            }
        })
    }

    fun <T> enqueueWithRetry(call: Call<T>, callback: Callback<T>, isLoaderShown: Boolean) {
        ActivityBase.activity.showAppLoader()
        call.clone().enqueue(callback)
    }
}
