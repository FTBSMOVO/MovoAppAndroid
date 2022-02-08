package com.movocash.movo.data.repository

import com.movocash.movo.MovoApp
import com.movocash.movo.data.model.requestmodel.*
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.remote.SingleEnqueueCall
import com.movocash.movo.data.remote.callback.IGenericCallBack
import com.movocash.movo.utilities.Constants
import com.movocash.movo.utilities.helper.SingleLiveData

class CardRepository : IGenericCallBack {

    var authResponseModel: SingleLiveData<GetCardAuthResponseModel> = SingleLiveData()
    var cardAccountResponseModel: SingleLiveData<GetCardAccountsResponseModel> = SingleLiveData()
    var miniStatementResponseModel: SingleLiveData<GetMiniStatementResponseModel> = SingleLiveData()
    var transactionHistoryResponseModel: SingleLiveData<GetTransactionHistoryResponseModel> = SingleLiveData()
    var authFailure: SingleLiveData<String> = SingleLiveData()
    var cardAccountFailure: SingleLiveData<String> = SingleLiveData()
    var miniStatementFailure: SingleLiveData<String> = SingleLiveData()
    var transactionHistoryFailure: SingleLiveData<String> = SingleLiveData()
    var shareFundResponseModel: SingleLiveData<ShareFundResponseModel> = SingleLiveData()
    var shareFundFailure: SingleLiveData<String> = SingleLiveData()
    var shareHistoryResponseModel: SingleLiveData<GetShareHistoryResponseModel> = SingleLiveData()
    var shareHistoryFailure: SingleLiveData<String> = SingleLiveData()
    var addCashCardResponseModel: SingleLiveData<ShareFundResponseModel> = SingleLiveData()
    var addCashCardFailure: SingleLiveData<String> = SingleLiveData()
    var loadUnloadCardResponseModel: SingleLiveData<ShareFundResponseModel> = SingleLiveData()
    var loadUnloadCashCardFailure: SingleLiveData<String> = SingleLiveData()
    var blockedResponseModel: SingleLiveData<Boolean> = SingleLiveData()
    var blockedFailure: SingleLiveData<String> = SingleLiveData()
    var unBlockedResponseModel: SingleLiveData<Boolean> = SingleLiveData()
    var changeNameFailure: SingleLiveData<String> = SingleLiveData()
    var changeNameResponseModel: SingleLiveData<Boolean> = SingleLiveData()
    var authTokenResponse: SingleLiveData<GetAuthTokenResponseModel> = SingleLiveData()
    var authTokenFailure :  SingleLiveData<String> = SingleLiveData()

    var ActivateCardResponse: SingleLiveData<GeneralResponseModel> = SingleLiveData()
    var ActivateCardFailure :  SingleLiveData<String> = SingleLiveData()

    var getUserScheduleReloadResponseModel: SingleLiveData<getUserScheduleReloadResponseModel> = SingleLiveData()
    var getUserScheduleReloadFailure :  SingleLiveData<String> = SingleLiveData()

    var addUserScheduleReloadSuccess: SingleLiveData<UpdatedGeneralResponseModel> = SingleLiveData()
    var addUserScheduleReloadFailure :  SingleLiveData<String> = SingleLiveData()

    fun getCardAuthData(referenceId: String) {
        val call = MovoApp.apiService.getAuthData(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_CARD_AUTH_DATA_URL, false, this)
    }

    fun getUserScheduleReload(referenceId: String) {
        val call = MovoApp.apiService.getUserScheduleReload(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_USERSCHEDULE_RELOAD, false, this)
    }

    fun getCardAccounts(isLoader: Boolean) {
        val call = MovoApp.apiService.getCardAccounts()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_CARDS_ACCOUNT_URL, isLoader, this)
    }

    fun ActivateCard(obj: ActivateCardModelApi) {
        val call = MovoApp.apiService.ActivateCard(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.ACTIVATE_CARD, true, this)
    }

    fun getMiniStatementAccounts(isLoader: Boolean) {
        val call = MovoApp.apiService.getMiniStatementAccounts()
        SingleEnqueueCall.callRetrofit(call, Constants.GET_MINI_STATEMENT_ACCOUNTS_URL, isLoader, this)
    }

    fun getTransactionHistory(referenceId: String, isLoader: Boolean) {
        val call = MovoApp.apiService.getTransactionHistory(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_TRANSACTION_HISTORY_URL, isLoader, this)
    }

    fun shareFund(obj: ShareFundRequestModel) {
        val call = MovoApp.apiService.shareFund(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.SHARE_FUND_URL, true, this)
    }

    fun addUserScheduleReloads(obj: AddUserScheduleReloadsRequestModel) {
        val call = MovoApp.apiService.AddUserScheduleReload(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.ADD_USERSCHEDULE_RELOAD, true, this)
    }

    fun getShareHistory(referenceId: String, isLoader: Boolean) {
        val call = MovoApp.apiService.getShareHistory(referenceId)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_SHARE_HISTORY_URL, isLoader, this)
    }

    fun addCashCard(obj: AddCashCardRequestModel) {
        val call = MovoApp.apiService.addCashCard(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.ADD_CASH_CARD_URL, true, this)
    }

    fun reloadCashCard(obj: LoadUnloadRequestModel) {
        val call = MovoApp.apiService.reloadCashCard(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.RELOAD_CASH_CARD_URL, true, this)
    }

    fun unloadCashCard(obj: LoadUnloadRequestModel) {
        val call = MovoApp.apiService.unloadCashCard(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.UNLOAD_CASH_CARD_URL, true, this)
    }

    fun blockCard(referenceId: String) {
        val call = MovoApp.apiService.blockUnblockCard(referenceId, true)
        SingleEnqueueCall.callRetrofit(call, Constants.BLOCK_UNBLOCK_CARD_URL, true, this)
    }

    fun unBlockCard(referenceId: String) {
        val call = MovoApp.apiService.blockUnblockCard(referenceId, false)
        SingleEnqueueCall.callRetrofit(call, Constants.UNBLOCK_CARD_URL, true, this)
    }

    fun changeCashCardName(obj: UpdateNameRequestModel) {
        val call = MovoApp.apiService.changeCashCardName(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.CHANGE_CASH_CARD_NAME, true, this)
    }

    fun getAuthToken(obj: GetAuthTokenRequestModel) {
        val call = MovoApp.apiService.getAuthToken(obj)
        SingleEnqueueCall.callRetrofit(call, Constants.GET_MOVO_TOKEN_URL, true, this)
    }

    override fun success(apiName: String, response: Any?) {
        when (apiName) {
            Constants.GET_CARD_AUTH_DATA_URL -> {
                val responseModel = response as GetCardAuthResponseModel
                if (response.isError) {
                    authFailure.postValue(response.messages!!)
                } else
                    authResponseModel.postValue(responseModel)
            }
            Constants.ACTIVATE_CARD -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    ActivateCardFailure.postValue(response.messages!!)
                } else
                    ActivateCardResponse.postValue(responseModel)
            }
            Constants.ADD_USERSCHEDULE_RELOAD -> {
                val responseModel = response as UpdatedGeneralResponseModel
                if (response.isError) {
                    addUserScheduleReloadFailure.postValue(response.messages!!)
                } else
                    addUserScheduleReloadSuccess.postValue(responseModel)
            }
            Constants.GET_USERSCHEDULE_RELOAD -> {
                val responseModel = response as getUserScheduleReloadResponseModel
                if (response.isError) {
                    getUserScheduleReloadFailure.postValue(response.messages!!)
                } else
                    getUserScheduleReloadResponseModel.postValue(responseModel)
            }
            Constants.GET_CARDS_ACCOUNT_URL -> {
                val responseModel = response as GetCardAccountsResponseModel
                if (response.isError) {
                    cardAccountFailure.postValue(response.messages!!)
                } else
                    cardAccountResponseModel.postValue(responseModel)
            }
            Constants.GET_MINI_STATEMENT_ACCOUNTS_URL -> {
                val responseModel = response as GetMiniStatementResponseModel
                if (response.isError) {
                    miniStatementFailure.postValue(response.messages!!)
                } else
                    miniStatementResponseModel.postValue(responseModel)
            }
            Constants.GET_TRANSACTION_HISTORY_URL -> {
                val responseModel = response as GetTransactionHistoryResponseModel
                if (response.isError) {
                    transactionHistoryFailure.postValue(response.messages!!)
                } else
                    transactionHistoryResponseModel.postValue(responseModel)
            }
            Constants.SHARE_FUND_URL -> {
                val responseModel = response as ShareFundResponseModel
                if (response.isError) {
                    shareFundFailure.postValue(response.messages!!)
                } else
                    shareFundResponseModel.postValue(responseModel)
            }
            Constants.GET_SHARE_HISTORY_URL -> {
                val responseModel = response as GetShareHistoryResponseModel
                if (response.isError) {
                    shareHistoryFailure.postValue(response.messages!!)
                } else
                    shareHistoryResponseModel.postValue(responseModel)
            }
            Constants.ADD_CASH_CARD_URL -> {
                val responseModel = response as ShareFundResponseModel
                if (response.isError) {
                    addCashCardFailure.postValue(response.messages!!)
                } else
                    addCashCardResponseModel.postValue(responseModel)
            }
            Constants.RELOAD_CASH_CARD_URL -> {
                val responseModel = response as ShareFundResponseModel
                if (response.isError) {
                    loadUnloadCashCardFailure.postValue(response.messages!!)
                } else
                    loadUnloadCardResponseModel.postValue(responseModel)
            }
            Constants.UNLOAD_CASH_CARD_URL -> {
                val responseModel = response as ShareFundResponseModel
                if (response.isError) {
                    loadUnloadCashCardFailure.postValue(response.messages!!)
                } else
                    loadUnloadCardResponseModel.postValue(responseModel)
            }
            Constants.BLOCK_UNBLOCK_CARD_URL -> {
                val responseModel = response as CardBlockedResponseModel
                if (response.isError) {
                    blockedFailure.postValue(response.messages!!)
                } else
                    blockedResponseModel.postValue(true)
            }
            Constants.UNBLOCK_CARD_URL -> {
                val responseModel = response as CardBlockedResponseModel
                if (response.isError) {
                    blockedFailure.postValue(response.messages!!)
                } else
                    unBlockedResponseModel.postValue(true)
            }
            Constants.CHANGE_CASH_CARD_NAME -> {
                val responseModel = response as GeneralResponseModel
                if (response.isError) {
                    changeNameFailure.postValue(response.messages!!)
                } else
                    changeNameResponseModel.postValue(true)
            }
            Constants.GET_MOVO_TOKEN_URL -> {
                val responseModel = response as GetAuthTokenResponseModel
                if (responseModel.isError) {
                    authTokenFailure.postValue(response.messages!!)
                } else
                    authTokenResponse.postValue(responseModel)
            }
        }
    }

    override fun failure(apiName: String, message: String?, response: Any?) {
        when (apiName) {
            Constants.GET_CARD_AUTH_DATA_URL -> {
                authFailure.postValue(message!!)
            }
            Constants.ACTIVATE_CARD -> {
                ActivateCardFailure.postValue(message!!)
            }
            Constants.ADD_USERSCHEDULE_RELOAD -> {
                addUserScheduleReloadFailure.postValue(message!!)
            }
            Constants.GET_USERSCHEDULE_RELOAD -> {
                getUserScheduleReloadFailure.postValue(message!!)
            }
            Constants.GET_CARDS_ACCOUNT_URL -> {
                cardAccountFailure.postValue(message!!)
            }
            Constants.GET_MINI_STATEMENT_ACCOUNTS_URL -> {
                miniStatementFailure.postValue(message!!)
            }
            Constants.GET_TRANSACTION_HISTORY_URL -> {
                transactionHistoryFailure.postValue(message!!)
            }
            Constants.SHARE_FUND_URL -> {
                shareFundFailure.postValue(message!!)
            }
            Constants.GET_SHARE_HISTORY_URL -> {
                shareHistoryFailure.postValue(message!!)
            }
            Constants.ADD_CASH_CARD_URL -> {
                addCashCardFailure.postValue(message!!)
            }
            Constants.RELOAD_CASH_CARD_URL -> {
                loadUnloadCashCardFailure.postValue(message!!)
            }
            Constants.UNLOAD_CASH_CARD_URL -> {
                loadUnloadCashCardFailure.postValue(message!!)
            }
            Constants.BLOCK_UNBLOCK_CARD_URL -> {
                blockedFailure.postValue(message!!)
            }
            Constants.UNBLOCK_CARD_URL -> {
                blockedFailure.postValue(message!!)
            }
            Constants.CHANGE_CASH_CARD_NAME -> {
                changeNameFailure.postValue(message!!)
            }
            Constants.GET_MOVO_TOKEN_URL -> {
                authTokenFailure.postValue(message!!)
            }
        }
    }

   /* override fun failure(apiName: String, message: String?, response: Any?) {
        TODO("Not yet implemented")
    }*/
}