package com.movocash.movo.data.repository

import android.text.TextUtils
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.AddEditPayeeRequestModel
import com.movocash.movo.data.model.requestmodel.MakeUpdatePaymentRequestModel
import com.movocash.movo.data.model.requestmodel.SearchPayeeRequestModel
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.remote.SingleEnqueueCall
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.helper.SingleLiveData

class ECheckBookRepository : IGenericCallBack {

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

    fun addUpdatePayee(obj: AddEditPayeeRequestModel, isEdit: Boolean) {
        if (!isEdit) {
            val call = MovoApp.apiService.addPayee(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.ADD_PAYEE_URL, true, this)
        } else {
            val call = MovoApp.apiService.editPayee(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.EDIT_PAYEE_URL, true, this)
        }
    }

    fun removePayee(accountSerialNo: String, accountNumber:String) {
        val call = MovoApp.apiService.removePayee(accountSerialNo,accountNumber)
        SingleEnqueueCall.callRetrofit(call, Constants.REMOVE_PAYEE_URL, true, this)
    }

    fun getMyPayeesList(isLoader: Boolean) {
        val call = MovoApp.apiService.getMyPayeesList()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_CARD_HOLDER_LIST_URL, isLoader, this)
    }

    fun getSearchedPayeesList(obj: SearchPayeeRequestModel) {
        val call = MovoApp.apiService.searchPayee(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SEARCH_PAYEE_URL, true, this)
    }

    fun makeUpdatePayment(obj: MakeUpdatePaymentRequestModel) {
        if (TextUtils.isEmpty(obj.tansId)) {
            val call = MovoApp.apiService.makePayment(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.MAKE_PAYMENT_URL, true, this)
        } else {
            val call = MovoApp.apiService.updatePayment(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.MAKE_PAYMENT_URL, true, this)
        }
    }

    fun cancelPayment(transId: String) {
        val call = MovoApp.apiService.cancelPayment(transId)
        SingleEnqueueCall.callRetrofit(call, Constants.CANCEL_PAYMENT_URL, true, this)
    }

    fun getPaymentHistory(referenceId: String,isLoader: Boolean) {
        val call = MovoApp.apiService.getPaymentHistory(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_PAYMENT_HISTORY_URL, isLoader, this)
    }



    override fun success(apiName: String, response: Any?) {
        when (apiName) {
            Constants.GET_CARD_HOLDER_LIST_URL -> {
                val responseModel = response as GetMyPayeeResponseModel
                if (response.isError) {
                    myPayeeFailure.postValue(response.messages!!)
                } else
                    myPayeeResponseModel.postValue(responseModel)
            }
            Constants.ADD_PAYEE_URL -> {
                val responseModel = response as PayeeResponseModel
                if (response.isError) {
                    payeeAddedOrUpdateFailure.postValue(response.messages!!)
                } else
                    payeeAddedOrUpdateResponseModel.postValue(responseModel)
            }
            Constants.EDIT_PAYEE_URL -> {
                val responseModel = response as PayeeResponseModel
                if (response.isError) {
                    payeeAddedOrUpdateFailure.postValue(response.messages!!)
                } else
                    payeeAddedOrUpdateResponseModel.postValue(responseModel)
            }
            Constants.REMOVE_PAYEE_URL -> {
                val responseModel = response as PayeeResponseModel
                if (response.isError) {
                    removeFailure.postValue(response.messages!!)
                } else
                    removeResponseModel.postValue(responseModel)
            }
            Constants.SEARCH_PAYEE_URL -> {
                val responseModel = response as GetSearchedPayeeResponseModel
                if (response.isError) {
                    searchPayeeFailure.postValue(response.messages!!)
                } else
                    searchPayeeResponseModel.postValue(responseModel)
            }
            Constants.MAKE_PAYMENT_URL -> {
                val responseModel = response as PaymentResponseModel
                if (response.isError) {
                    makeUpdatePaymentFailure.postValue(response.messages!!)
                } else
                    makeUpdatePaymentResponseModel.postValue(responseModel)
            }
            Constants.CANCEL_PAYMENT_URL -> {
                val responseModel = response as PaymentResponseModel
                if (response.isError) {
                    cancelPaymentFailure.postValue(response.messages!!)
                } else
                    cancelPaymentResponseModel.postValue(responseModel)
            }
            Constants.GET_PAYMENT_HISTORY_URL -> {
                val responseModel = response as GetPaymentHistoryResponseModel
                if (response.isError) {
                    paymentHistoryFailure.postValue(response.messages!!)
                } else
                    paymentHistoryResponseModel.postValue(responseModel)
            }
        }
    }

    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            Constants.GET_CARD_HOLDER_LIST_URL -> {
                myPayeeFailure.postValue(message)
            }
            Constants.ADD_PAYEE_URL -> {
                payeeAddedOrUpdateFailure.postValue(message)
            }
            Constants.EDIT_PAYEE_URL -> {
                payeeAddedOrUpdateFailure.postValue(message)
            }
            Constants.REMOVE_PAYEE_URL -> {
                removeFailure.postValue(message)
            }
            Constants.SEARCH_PAYEE_URL -> {
                searchPayeeFailure.postValue(message)
            }
            Constants.MAKE_PAYMENT_URL -> {
                makeUpdatePaymentFailure.postValue(message)
            }
            Constants.CANCEL_PAYMENT_URL -> {
                cancelPaymentFailure.postValue(message)
            }
            Constants.GET_PAYMENT_HISTORY_URL -> {
                paymentHistoryFailure.postValue(message)
            }
        }
    }

//    override fun failure(apiName: String, message: String?, response: Any?) {
//        TODO("Not yet implemented")
//    }
}