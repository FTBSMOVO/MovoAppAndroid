package com.movocash.movo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.movocash.movo.data.model.requestmodel.CashOutToBankRequestModel
import com.movocash.movo.data.model.requestmodel.CashintoMovoRequestModel
import com.movocash.movo.data.model.requestmodel.CreateBankAccountRequestModel
import com.movocash.movo.data.model.requestmodel.PlaidCreateBankAccountRequestModel
import com.movocash.movo.data.model.responsemodel.*
import com.movocash.movo.data.repository.BankRepository
import com.movocash.movo.utilities.helper.SingleLiveData

class BankViewModel(application: Application) : AndroidViewModel(application) {

    var repository = BankRepository()
    var createBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var createBankAccountFailure: SingleLiveData<String> = SingleLiveData()

    var plaidcreateBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var plaidcreateBankAccountFailure: SingleLiveData<String> = SingleLiveData()

    var getBankAccountsResponseModel: SingleLiveData<GetBankAccountsResponseModel> = SingleLiveData()
    var getBankAccountsFailure: SingleLiveData<String> = SingleLiveData()
    var editBankAccountResponseModel: SingleLiveData<CreateBankAccountResponseModel> = SingleLiveData()
    var editBankAccountFailure: SingleLiveData<String> = SingleLiveData()
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

    init {

        getPlaidTokenSuccess = repository.getPlaidTokenSuccess
        getPlaidTokenFailure = repository.getPlaidTokenFailure

        createBankAccountResponseModel = repository.createBankAccountResponseModel
        createBankAccountFailure = repository.createBankAccountFailure

        plaidcreateBankAccountResponseModel = repository.plaidcreateBankAccountResponseModel
        plaidcreateBankAccountFailure = repository.plaidcreateBankAccountFailure

        editBankAccountResponseModel = repository.editBankAccountResponseModel
        editBankAccountFailure = repository.editBankAccountFailure
        getBankAccountsResponseModel = repository.getBankAccountsResponseModel
        getBankAccountsFailure = repository.getBankAccountsFailure
        removeBankAccountResponseModel = repository.removeBankAccountResponseModel
        removeBankAccountFailure = repository.removeBankAccountFailure


        cashToBankTransferResponseModel = repository.cashToBankTransferResponseModel
        cashToBankTransferFailure = repository.cashToBankTransferFailure

        bankToCashTransferResponseModel = repository.bankToCashTransferResponseModel
        bankToCashTransferFailure = repository.bankToCashTransferFailure


        scheduleTransferListResponseModel = repository.scheduleTransferListResponseModel
        scheduleTransferListFailure = repository.scheduleTransferListFailure
        cancelScheduledTransferResponseModel = repository.cancelScheduledTransferResponseModel
        cancelScheduledTransferFailure = repository.cancelScheduledTransferFailure
    }

    fun createBankAccount(obj: CreateBankAccountRequestModel) {
        repository.createBankAccount(obj)
    }

    fun plaidcreateBankAccount(obj: PlaidCreateBankAccountRequestModel) {
        repository.plaidcreateBankAccount(obj)
    }

    fun getBankAccounts(isLoader: Boolean) {
        repository.getBanksAccount(isLoader)
    }

    fun getPlaidToken(isLoader: Boolean) {
        repository.getPlaidToken(isLoader)
    }

    fun removeBankAccount(accountSerialNo: String) {
        repository.removeBankAccount(accountSerialNo)
    }

    fun cashToBankTransfer(obj: CashOutToBankRequestModel) {
        repository.cashToBankTransfer(obj)
    }

    fun bankToCashTransfer(obj: CashintoMovoRequestModel) {
        repository.bankToCashTransfer(obj)
    }

    fun bankToCashTransferUpdate(obj: CashOutToBankRequestModel) {
        repository.bankToCashTransferUpdate(obj)
    }



    fun getScheduleTransferList(referenceId: String, isLoader: Boolean) {
        repository.getScheduleTransferList(referenceId,isLoader)
    }

    fun getScheduleTransferListPlaid(referenceId: String, isLoader: Boolean) {
        repository.getScheduleTransferListPlaid(referenceId,isLoader)
    }

    fun cancelScheduledTransfer(transferId: String) {
        repository.cancelScheduledTransfer(transferId)
    }

    fun cancelBtcScheduledTransfer(transferId: String) {
        repository.cancelBtcScheduledTransfer(transferId)
    }
}