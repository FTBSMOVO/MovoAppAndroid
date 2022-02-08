package com.movocash.movo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.movocash.movo.data.model.requestmodel.*
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.repository.CardRepository
import com.movocash.movo.utilities.helper.SingleLiveData

class CardViewModel(application: Application) : AndroidViewModel(application) {

    var repository = CardRepository()
    var cardAccountResponseModel: SingleLiveData<GetCardAccountsResponseModel> = SingleLiveData()
    var miniStatementResponseModel: SingleLiveData<GetMiniStatementResponseModel> = SingleLiveData()
    var transactionHistoryResponseModel: SingleLiveData<GetTransactionHistoryResponseModel> = SingleLiveData()
    var authFailure: SingleLiveData<String> = SingleLiveData()
    var cardAccountFailure: SingleLiveData<String> = SingleLiveData()
    var miniStatementFailure: SingleLiveData<String> = SingleLiveData()
    var transactionHistoryFailure: SingleLiveData<String> = SingleLiveData()
    var authResponseModel: SingleLiveData<GetCardAuthResponseModel> = SingleLiveData()
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

    var getUserScheduleReloadResponseModel: SingleLiveData<getUserScheduleReloadResponseModel> = SingleLiveData()
    var getUserScheduleReloadFailure :  SingleLiveData<String> = SingleLiveData()

    var addUserScheduleReloadSuccess: SingleLiveData<UpdatedGeneralResponseModel> = SingleLiveData()
    var addUserScheduleReloadFailure :  SingleLiveData<String> = SingleLiveData()

    var ActivateCardResponse: SingleLiveData<GeneralResponseModel> = SingleLiveData()
    var ActivateCardFailure :  SingleLiveData<String> = SingleLiveData()

    init {

        ActivateCardResponse = repository.ActivateCardResponse
        ActivateCardFailure = repository.ActivateCardFailure

        cardAccountResponseModel = repository.cardAccountResponseModel
        miniStatementResponseModel = repository.miniStatementResponseModel
        transactionHistoryResponseModel = repository.transactionHistoryResponseModel
        authFailure = repository.authFailure
        cardAccountFailure = repository.cardAccountFailure
        miniStatementFailure = repository.miniStatementFailure
        transactionHistoryFailure = repository.transactionHistoryFailure
        authResponseModel = repository.authResponseModel
        shareFundResponseModel = repository.shareFundResponseModel
        shareFundFailure = repository.shareFundFailure
        shareHistoryResponseModel = repository.shareHistoryResponseModel
        shareHistoryFailure = repository.shareHistoryFailure
        addCashCardResponseModel = repository.addCashCardResponseModel
        addCashCardFailure = repository.addCashCardFailure
        loadUnloadCardResponseModel = repository.loadUnloadCardResponseModel
        loadUnloadCashCardFailure = repository.loadUnloadCashCardFailure
        blockedResponseModel = repository.blockedResponseModel
        blockedFailure = repository.blockedFailure
        unBlockedResponseModel = repository.unBlockedResponseModel
        changeNameFailure = repository.changeNameFailure
        changeNameResponseModel = repository.changeNameResponseModel
        authTokenResponse = repository.authTokenResponse
        authTokenFailure = repository.authTokenFailure

        getUserScheduleReloadResponseModel = repository.getUserScheduleReloadResponseModel
        getUserScheduleReloadFailure = repository.getUserScheduleReloadFailure

        addUserScheduleReloadSuccess = repository.addUserScheduleReloadSuccess
        addUserScheduleReloadFailure = repository.addUserScheduleReloadFailure
    }

    fun getCardAuthData(referenceId: String) {
        repository.getCardAuthData(referenceId)
    }

    fun getUserScheduleReload(referenceId: String) {
        repository.getUserScheduleReload(referenceId)
    }

    fun addUserScheduleReload(obj: AddUserScheduleReloadsRequestModel) {
        repository.addUserScheduleReloads(obj)
    }

    fun activateCard(obj: ActivateCardModelApi) {
        repository.ActivateCard(obj)
    }

    fun getCardAccounts(isLoader: Boolean) {
        repository.getCardAccounts(isLoader)
    }

    fun getMiniStatementAccounts(isLoader: Boolean) {
        repository.getMiniStatementAccounts(isLoader)
    }

    fun getTransactionHistory(referenceId: String, isLoader: Boolean) {
        repository.getTransactionHistory(referenceId, isLoader)
    }

    fun shareFund(obj: ShareFundRequestModel) {
        repository.shareFund(obj)
    }

    fun getShareHistory(referenceId: String, isLoader: Boolean) {
        repository.getShareHistory(referenceId, isLoader)
    }

    fun addCashCard(obj: AddCashCardRequestModel) {
        repository.addCashCard(obj)
    }

    fun reloadCashCard(obj: LoadUnloadRequestModel) {
        repository.reloadCashCard(obj)
    }

    fun unloadCashCard(obj: LoadUnloadRequestModel) {
        repository.unloadCashCard(obj)
    }

    fun blockCard(referenceId: String) {
        repository.blockCard(referenceId)
    }

    fun unBlockCard(referenceId: String) {
        repository.unBlockCard(referenceId)
    }

    fun changeCashCardName(obj: UpdateNameRequestModel) {
        repository.changeCashCardName(obj)
    }

    fun getAuthToken(obj: GetAuthTokenRequestModel) {
        repository.getAuthToken(obj)
    }
}