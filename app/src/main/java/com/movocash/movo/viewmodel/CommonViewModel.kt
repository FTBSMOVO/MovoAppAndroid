package com.movocash.movo.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.repository.CommonRepository
import com.movocash.movo.utilities.helper.SingleLiveData

class CommonViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = CommonRepository()
    var countriesResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var statesResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var identificationResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var questionsResponse: SingleLiveData<IDNameResponseModel> = SingleLiveData()
    var serviceFeeResponseModel: SingleLiveData<GetServiceFeeResponseModel> = SingleLiveData()
    var serviceFailure: SingleLiveData<String> = SingleLiveData()
    var urlResponseModel: SingleLiveData<UploadMediaResponseModel> = SingleLiveData()
    var urlFailure: SingleLiveData<String> = SingleLiveData()

    var getFrequesnciesResponse: SingleLiveData<GetFrequesnciesResponseModel> = SingleLiveData()
    var getFrequencyFailure: SingleLiveData<String> = SingleLiveData()

    var sideMenuResponse: SingleLiveData<sideMenuResponseModel> = SingleLiveData()
    var sideMenuFailure: SingleLiveData<String> = SingleLiveData()

    init {
        countriesResponse = repository.countriesResponse
        statesResponse = repository.statesResponse
        identificationResponse = repository.identificationResponse
        questionsResponse = repository.questionsResponse
        serviceFailure = repository.serviceFailure
        serviceFeeResponseModel = repository.serviceFeeResponseModel
        urlResponseModel = repository.urlResponseModel
        urlFailure = repository.urlFailure

        getFrequesnciesResponse = repository.getFrequenciesResponse
        getFrequencyFailure = repository.getFrequenceFailure

        sideMenuResponse = repository.sideMenuResponse
        sideMenuFailure = repository.sideMenuFailure


    }

    fun getCountries() {
        repository.getCountries()
    }

    fun getFrequencies() {
        repository.getFrequencies()
    }

    fun getConfigurations() {
        repository.getConfigurations()
    }

    fun getStateByCountry(countryId: Int) {
        repository.getStateByCountry(countryId)
    }

    fun getIdentificationTypes() {
        repository.getIdentificationTypes()
    }

    fun getQuestions() {
        repository.getQuestions()
    }

    fun getServiceFee(referenceId: String, feeType: Int) {
        repository.getServiceFee(referenceId, feeType)
    }

    fun uploadFullImage(mediaUri: Uri?) {
        repository.uploadFullImage(mediaUri)
    }
}