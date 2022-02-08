package com.movocash.movo.data.repository

import android.net.Uri
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.remote.SingleEnqueueCall
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.extensions.showToastMessage
import com.movocash.movo.utilities.helper.SingleLiveData
import com.movocash.movo.view.ui.base.ActivityBase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CommonRepository : IGenericCallBack {
    var countriesResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var statesResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var identificationResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var questionsResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var serviceFeeResponseModel: SingleLiveData<GetServiceFeeResponseModel> = SingleLiveData()
    var serviceFailure: SingleLiveData<String> = SingleLiveData()
    var urlResponseModel: SingleLiveData<UploadMediaResponseModel> = SingleLiveData()
    var urlFailure: SingleLiveData<String> = SingleLiveData()

    var getFrequenciesResponse: SingleLiveData<GetFrequesnciesResponseModel> = SingleLiveData()
    var getFrequenceFailure: SingleLiveData<String> = SingleLiveData()

    var sideMenuResponse: SingleLiveData<sideMenuResponseModel> = SingleLiveData()
    var sideMenuFailure: SingleLiveData<String> = SingleLiveData()

    fun getCountries() {
        val call = MovoApp.apiService.getCountries()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_COUNTRIES_URL, false, this)
    }

    fun getFrequencies() {
        val call = MovoApp.apiService.getFrequencies()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_FREQUENCIES, false, this)
    }

    fun getConfigurations() {
        val call = MovoApp.apiService.getConfigurations()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_CONFIGURATIONS, false, this)
    }

    fun getStateByCountry(countryId: Int) {
        val call = MovoApp.apiService.getStates(countryId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_STATES_URL, false, this)
    }

    fun getServiceFee(referenceId: String, feeType:Int) {
        val call = MovoApp.apiService.getServiceFee(referenceId, feeType)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_SERVICE_FEE, true, this)
    }

    fun getIdentificationTypes() {
        val call = MovoApp.apiService.getIdentificationTypes()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_IDENTIFICATION_URL, false, this)
    }

    fun getQuestions() {
        val call = MovoApp.apiService.getQuestions()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_QUESTIONS_URL, false, this)
    }

    fun uploadFullImage(mediaUri: Uri?) {
        val filePart: MultipartBody.Part?
        val filesList = ArrayList<MultipartBody.Part>()
        if (mediaUri != null) {
            val file = File(mediaUri.path!!)
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            filePart = if (file.extension.isNotEmpty()) {
                val fileBody = MultipartBody.Part.createFormData("mediaFiles", "profilePhoto." + file.extension, reqFile)
                fileBody
            } else {
                //changed jpg to jpeg
                val fileBody = MultipartBody.Part.createFormData("mediaFiles", "profilePhoto.jpeg", reqFile)
                fileBody
            }
            filesList.add(filePart)
            val call = MovoApp.apiService.uploadMedia(filesList, Constants.CONST_IMAGE.toInt())
            SingleEnqueueCall.callRetrofit(call, Constants.GET_PIC_URL, false, this)
        }
    }

    override fun success(apiName: String, response: Any?) {
        when (apiName) {
            Constants.GET_COUNTRIES_URL -> {
                val responseModel = response as IDNameResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    countriesResponse.postValue(responseModel)
            }
            Constants.GET_FREQUENCIES -> {
                val responseModel = response as GetFrequesnciesResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    getFrequenciesResponse.postValue(responseModel)
            }
            Constants.GET_CONFIGURATIONS -> {
                val responseModel = response as sideMenuResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    sideMenuResponse.postValue(responseModel)
            }

            Constants.GET_STATES_URL -> {
                val responseModel = response as IDNameResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    statesResponse.postValue(responseModel)
            }
            Constants.GET_IDENTIFICATION_URL -> {
                val responseModel = response as IDNameResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    identificationResponse.postValue(responseModel)
            }
            Constants.GET_QUESTIONS_URL -> {
                val responseModel = response as IDNameResponseModel
                if (response.isError) {
                    ActivityBase.activity.showToastMessage(response.messages!!)
                } else
                    questionsResponse.postValue(responseModel)
            }
            Constants.GET_SERVICE_FEE -> {
                val responseModel = response as GetServiceFeeResponseModel
                if (response.isError) {
                    serviceFailure.postValue(response.messages!!)
                } else
                    serviceFeeResponseModel.postValue(responseModel)
            }
            Constants.GET_PIC_URL -> {
                val responseModel = response as UploadMediaResponseModel
                if (response.isError) {
                    urlFailure.postValue(response.messages!!)
                } else
                    urlResponseModel.postValue(responseModel)
            }
        }
    }

    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            Constants.GET_COUNTRIES_URL -> {
                ActivityBase.activity.showToastMessage(message!!)
            }
            Constants.GET_FREQUENCIES -> {
                ActivityBase.activity.showToastMessage(message!!)
                getFrequenceFailure.postValue(message)
            }
            Constants.GET_CONFIGURATIONS -> {
                ActivityBase.activity.showToastMessage(message!!)
                sideMenuFailure.postValue(message)
            }
            Constants.GET_STATES_URL -> {
                ActivityBase.activity.showToastMessage(message!!)
            }
            Constants.GET_IDENTIFICATION_URL -> {
                ActivityBase.activity.showToastMessage(message!!)
            }
            Constants.GET_QUESTIONS_URL -> {
                ActivityBase.activity.showToastMessage(message!!)
            }
            Constants.GET_SERVICE_FEE -> {
                serviceFailure.postValue(message)
            }
            Constants.GET_PIC_URL -> {
                urlFailure.postValue(message)
            }
        }
    }

//    override fun failure(apiName: String, message: String?, response: Any?) {
//        TODO("Not yet implemented")
//    }
}