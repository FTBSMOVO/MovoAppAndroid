package com.movocash.movo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.movocash.movo.data.model.requestmodel.AddEditPayeeRequestModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.data.model.requestmodel.SearchPayeeRequestModel
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.repository.ECheckBookRepository
import com.movocash.movo.utilities.helper.SingleLiveData

class ECheckBookViewModel(application: Application) : AndroidViewModel(application) {

    var repository = ECheckBookRepository()
    var myPayeeFailure: SingleLiveData<String> = SingleLiveData()
    var myPayeeResponseModel: SingleLiveData<GetMyPayeeResponseModel> = SingleLiveData()
    var payeeAddedOrUpdateFailure: SingleLiveData<String> = SingleLiveData()
    var payeeAddedOrUpdateResponseModel: SingleLiveData<PayeeResponseModel> = SingleLiveData()
    var removeFailure: SingleLiveData<String> = SingleLiveData()
    var removeResponseModel: SingleLiveData<PayeeResponseModel> = SingleLiveData()
    var searchPayeeFailure: SingleLiveData<String> = SingleLiveData()
    var searchPayeeResponseModel: SingleLiveData<GetSearchedPayeeResponseModel> = SingleLiveData()
    var makeUpdatePaymentFailure: SingleLiveData<String> = SingleLiveData()
    var makeUpdatePaymentResponseModel: SingleLiveData<PaymentResponseModel> = SingleLiveData()
    var cancelPaymentFailure: SingleLiveData<String> = SingleLiveData()
    var cancelPaymentResponseModel: SingleLiveData<PaymentResponseModel> = SingleLiveData()
    var paymentHistoryFailure: SingleLiveData<String> = SingleLiveData()
    var paymentHistoryResponseModel: SingleLiveData<GetPaymentHistoryResponseModel> = SingleLiveData()

    init {
        myPayeeFailure = repository.myPayeeFailure
        myPayeeResponseModel = repository.myPayeeResponseModel
        payeeAddedOrUpdateFailure = repository.payeeAddedOrUpdateFailure
        payeeAddedOrUpdateResponseModel = repository.payeeAddedOrUpdateResponseModel
        removeFailure = repository.removeFailure
        removeResponseModel = repository.removeResponseModel
        searchPayeeFailure = repository.searchPayeeFailure
        searchPayeeResponseModel = repository.searchPayeeResponseModel
        makeUpdatePaymentFailure = repository.makeUpdatePaymentFailure
        makeUpdatePaymentResponseModel = repository.makeUpdatePaymentResponseModel
        cancelPaymentFailure = repository.cancelPaymentFailure
        cancelPaymentResponseModel = repository.cancelPaymentResponseModel
        paymentHistoryFailure = repository.paymentHistoryFailure
        paymentHistoryResponseModel = repository.paymentHistoryResponseModel
    }

    fun getMyPayeesList(isLoader: Boolean) {
        repository.getMyPayeesList(isLoader)
    }

    fun addUpdatePayee(obj: AddEditPayeeRequestModel, isEdit: Boolean) {
        repository.addUpdatePayee(obj, isEdit)
    }

    fun removePayee(accountSerialNo: String, accountNumber: String) {
        repository.removePayee(accountSerialNo, accountNumber)
    }

    fun getSearchedPayeesList(obj: SearchPayeeRequestModel) {
        repository.getSearchedPayeesList(obj)
    }

    fun makeUpdatePayment(obj: MakeUpdatePaymentRequestModel) {
        repository.makeUpdatePayment(obj)
    }

    fun cancelPayment(transId: String) {
        repository.cancelPayment(transId)
    }

    fun getPaymentHistory(referenceId: String,isLoader: Boolean) {
        repository.getPaymentHistory(referenceId,isLoader)
    }

}