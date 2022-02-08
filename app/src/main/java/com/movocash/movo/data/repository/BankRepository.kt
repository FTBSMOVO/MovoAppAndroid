package com.movocash.movo.data.repository

import android.text.TextUtils
import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.CashOutToBankRequestModel
import com.movocash.movo.data.model.requestmodel.CashintoMovoRequestModel
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.data.model.requestmodel.PlaidCreateBankAccountRequestModel
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.remote.SingleEnqueueCall
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.helper.SingleLiveData

class BankRepository : IGenericCallBack {

    var createBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var createBankAccountFailure: SingleLiveData<String> = SingleLiveData()

    var editBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var editBankAccountFailure: SingleLiveData<String> = SingleLiveData()
    var getBankAccountsResponseModel: SingleLiveData<GetBankAccountsResponseModel> = SingleLiveData()
    var getBankAccountsFailure: SingleLiveData<String> = SingleLiveData()
    var removeBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var removeBankAccountFailure: SingleLiveData<String> = SingleLiveData()


    var cashToBankTransferResponseModel: SingleLiveData<CashToBankResponseModel> = SingleLiveData()
    var cashToBankTransferFailure: SingleLiveData<String> = SingleLiveData()

    var bankToCashTransferResponseModel: SingleLiveData<CashToBankResponseModel> = SingleLiveData()
    var bankToCashTransferFailure: SingleLiveData<String> = SingleLiveData()


    var scheduleTransferListResponseModel: SingleLiveData<GetScheduledTransfersResponseModel> = SingleLiveData()
    var scheduleTransferListFailure: SingleLiveData<String> = SingleLiveData()
    var cancelScheduledTransferResponseModel: SingleLiveData<CashToBankResponseModel> = SingleLiveData()
    var cancelScheduledTransferFailure: SingleLiveData<String> = SingleLiveData()

    var getPlaidTokenSuccess: SingleLiveData<UpdatedGeneralResponseModel> = SingleLiveData()
    var getPlaidTokenFailure: SingleLiveData<String> = SingleLiveData()

    var plaidcreateBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var plaidcreateBankAccountFailure: SingleLiveData<String> = SingleLiveData()

    fun createBankAccount(obj: CreateBankAccountRequestModel) {
        if (!TextUtils.isEmpty(obj.bankSerialNumberIfEdit)) {
            val call = MovoApp.apiService.editBankAccount(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.EDIT_BANK_ACCOUNT_URL, true, this)
        } else {
            val call = MovoApp.apiService.createBankAccount(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.CREATE_BANK_ACCOUNT_URL, true, this)
        }
    }

    fun plaidcreateBankAccount(obj: PlaidCreateBankAccountRequestModel) {

            val call = MovoApp.apiService.plaidcreateBankAccount(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.PLAID_CREATE_BANK_ACCOUNT_URL, true, this)

    }

    fun removeBankAccount(accountSerialNo: String) {
        val call = MovoApp.apiService.removeBankAccount(accountSerialNo)
        SingleEnqueueCall.callRetrofit(call, Constants.REMOVE_BANK_ACCOUNT_URL, true, this)
    }

    fun getBanksAccount(isLoader: Boolean) {
        val call = MovoApp.apiService.getBankAccounts()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_BANK_ACCOUNTS_URL, isLoader, this)
    }

    fun cashToBankTransfer(obj: CashOutToBankRequestModel) {
        if (TextUtils.isEmpty(obj.transferId)) {
            val call = MovoApp.apiService.cashToBankTransfer(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.CASH_TO_BANK_TRANSFER_URL, true, this)
        } else {
            val call = MovoApp.apiService.cashToBankTransferUpdate(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.CASH_TO_BANK_TRANSFER_URL, true, this)
        }
    }




    fun bankToCashTransfer(obj: CashintoMovoRequestModel) {

            val call = MovoApp.apiService.bankToCashTransfer(obj)
            SingleEnqueueCall.callRetrofit(call, Constants.BANK_TO_CASH_TRANSFER_URL, true, this)
    }

    fun bankToCashTransferUpdate(obj: CashOutToBankRequestModel) {

        val call = MovoApp.apiService.bankToCashTransferUpdate(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.BANK_TO_CASH_TRANSFER_UPDATE_URL, true, this)

    }

    fun getScheduleTransferList(referenceId: String, isLoader: Boolean) {
        val call = MovoApp.apiService.getCashBankTransferList(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_CASH_TO_BANK_TRANSFER_LIST_URL, isLoader, this)
    }

    fun getScheduleTransferListPlaid(referenceId: String, isLoader: Boolean) {
        val call = MovoApp.apiService.getBankCashTransferList(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_BANK_TO_CASH_TRANSFER_LIST_URL, isLoader, this)
    }

    fun getShareHistory(referenceId: String, isLoader: Boolean) {
        val call = MovoApp.apiService.getShareHistory(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_SHARE_HISTORY_URL, isLoader, this)
    }

    fun cancelScheduledTransfer(transferId: String) {
        val call = MovoApp.apiService.cancelScheduledTransfer(transferId)
        SingleEnqueueCall.callRetrofit(call, Constants.CASH_TO_BANK_TRANSFER_CANCEL_URL, true, this)
    }

    fun cancelBtcScheduledTransfer(transferId: String) {
        val call = MovoApp.apiService.cancelBtcScheduledTransfer(transferId)
        SingleEnqueueCall.callRetrofit(call, Constants.BANK_TO_CASH_TRANSFER_CANCEL_URL, true, this)
    }

    fun getPlaidToken(isLoader: Boolean) {
        val call = MovoApp.apiService.getPlaidToken()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_PLAID_TOKEN, isLoader, this)
    }

    override fun success(apiName: String, response: Any?) {
        when (apiName) {
            Constants.GET_PLAID_TOKEN -> {
                val responseModel = response as UpdatedGeneralResponseModel
                if (response.isError) {
                    getBankAccountsFailure.postValue(response.messages!!)
                } else
                    getPlaidTokenSuccess.postValue(responseModel)
            }
            Constants.CREATE_BANK_ACCOUNT_URL -> {
                val responseModel = response as CreateBankAccountResponseModel
                if (response.isError) {
                    createBankAccountFailure.postValue(response.messages!!)
                } else
                    createBankAccountResponseModel.postValue(responseModel)
            }
            Constants.PLAID_CREATE_BANK_ACCOUNT_URL -> {
                val responseModel = response as CreateBankAccountResponseModel
                if (response.isError) {
                    plaidcreateBankAccountFailure.postValue(response.messages!!)
                } else
                    plaidcreateBankAccountResponseModel.postValue(responseModel)
            }
            Constants.EDIT_BANK_ACCOUNT_URL -> {
                val responseModel = response as CreateBankAccountResponseModel
                if (response.isError) {
                    editBankAccountFailure.postValue(response.messages!!)
                } else
                    editBankAccountResponseModel.postValue(responseModel)
            }
            Constants.REMOVE_BANK_ACCOUNT_URL -> {
                val responseModel = response as CreateBankAccountResponseModel
                if (response.isError) {
                    removeBankAccountFailure.postValue(response.messages!!)
                } else
                    removeBankAccountResponseModel.postValue(responseModel)
            }
            Constants.GET_BANK_ACCOUNTS_URL -> {
                val responseModel = response as GetBankAccountsResponseModel
                if (response.isError) {
                    getBankAccountsFailure.postValue(response.messages!!)
                } else
                    getBankAccountsResponseModel.postValue(responseModel)
            }
            Constants.CASH_TO_BANK_TRANSFER_URL -> {
                val responseModel = response as CashToBankResponseModel
                if (response.isError) {
                    cashToBankTransferFailure.postValue(response.messages!!)
                } else
                    cashToBankTransferResponseModel.postValue(responseModel)
            }
            Constants.BANK_TO_CASH_TRANSFER_URL -> {
                val responseModel = response as CashToBankResponseModel
                if (response.isError) {
                    bankToCashTransferFailure.postValue(response.messages!!)
                } else
                   bankToCashTransferResponseModel.postValue(responseModel)
            }
            Constants.BANK_TO_CASH_TRANSFER_UPDATE_URL -> {
                val responseModel = response as CashToBankResponseModel
                if (response.isError) {
                    bankToCashTransferFailure.postValue(response.messages!!)
                } else
                    bankToCashTransferResponseModel.postValue(responseModel)
            }
            Constants.GET_CASH_TO_BANK_TRANSFER_LIST_URL -> {
                val responseModel = response as GetScheduledTransfersResponseModel
                if (response.isError) {
                    scheduleTransferListFailure.postValue(response.messages!!)
                } else
                    scheduleTransferListResponseModel.postValue(responseModel)
            }
            Constants.GET_BANK_TO_CASH_TRANSFER_LIST_URL -> {
                val responseModel = response as GetScheduledTransfersResponseModel
                if (response.isError) {
                    scheduleTransferListFailure.postValue(response.messages!!)
                } else
                    scheduleTransferListResponseModel.postValue(responseModel)
            }


            Constants.CASH_TO_BANK_TRANSFER_CANCEL_URL -> {
                val responseModel = response as CashToBankResponseModel
                if (response.isError) {
                    cancelScheduledTransferFailure.postValue(response.messages!!)
                } else
                    cancelScheduledTransferResponseModel.postValue(responseModel)
            }
            Constants.BANK_TO_CASH_TRANSFER_CANCEL_URL -> {
                val responseModel = response as CashToBankResponseModel
                if (response.isError) {
                    cancelScheduledTransferFailure.postValue(response.messages!!)
                } else
                    cancelScheduledTransferResponseModel.postValue(responseModel)
            }
        }
    }

    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            Constants.GET_PLAID_TOKEN -> {
                getPlaidTokenFailure.postValue(message!!)
            }
            Constants.CREATE_BANK_ACCOUNT_URL -> {
                createBankAccountFailure.postValue(message!!)
            }
            Constants.PLAID_CREATE_BANK_ACCOUNT_URL -> {
                plaidcreateBankAccountFailure.postValue(message!!)
            }
            Constants.EDIT_BANK_ACCOUNT_URL -> {
                editBankAccountFailure.postValue(message!!)
            }
            Constants.REMOVE_BANK_ACCOUNT_URL -> {
                removeBankAccountFailure.postValue(message!!)
            }
            Constants.GET_BANK_ACCOUNTS_URL -> {
                createBankAccountFailure.postValue(message!!)
            }
            Constants.CASH_TO_BANK_TRANSFER_URL -> {
                cashToBankTransferFailure.postValue(message!!)
            }
            Constants.BANK_TO_CASH_TRANSFER_URL -> {
                bankToCashTransferFailure.postValue(message!!)
            }
            Constants.BANK_TO_CASH_TRANSFER_UPDATE_URL -> {
                bankToCashTransferFailure.postValue(message!!)
            }
            Constants.GET_CASH_TO_BANK_TRANSFER_LIST_URL -> {
                scheduleTransferListFailure.postValue(message!!)
            }
            Constants.GET_BANK_TO_CASH_TRANSFER_LIST_URL -> {
                scheduleTransferListFailure.postValue(message!!)
            }
            Constants.CASH_TO_BANK_TRANSFER_CANCEL_URL -> {
                cancelScheduledTransferFailure.postValue(message!!)
            }
            Constants.BANK_TO_CASH_TRANSFER_CANCEL_URL -> {
                cancelScheduledTransferFailure.postValue(message!!)
            }
        }
    }

  /*  override fun failure(apiName: String, message: String?, response: Any?) {
        TODO("Not yet implemented")
    }*/
}