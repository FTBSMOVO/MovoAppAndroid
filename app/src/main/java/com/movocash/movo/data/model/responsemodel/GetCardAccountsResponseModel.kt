package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetCardAccountsResponseModel {
    @SerializedName("isError")
    @Expose
    var isError = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var obj: Data? = null

    class Data{
        @SerializedName("responseCode")
        @Expose
        var responseCode: String? = null

        @SerializedName("responseDesc")
        @Expose
        var responseDesc: String? = null

        @SerializedName("cards")
        @Expose
        var cards: ArrayList<Card>? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("clerkID")
        @Expose
        var clerkID: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null
    }

    class Card{
        @SerializedName("isPrimaryCard")
        @Expose
        var isPrimaryCard: Int = 0

        @SerializedName("isPrimaryCardSpecified")
        @Expose
        var isPrimaryCardSpecified = false

        @SerializedName("cardNumber")
        @Expose
        var cardNumber: String? = null

        var cvv: String = ""

        @SerializedName("referenceID")
        @Expose
        var referenceID: String? = null

        @SerializedName("cardCategory")
        @Expose
        var cardCategory: String? = null

        @SerializedName("productType")
        @Expose
        var productType: String? = null

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("primaryCardNumber")
        @Expose
        var primaryCardNumber: String? = null

        @SerializedName("primaryCardReferenceID")
        @Expose
        var primaryCardReferenceID: String? = null

        @SerializedName("purseSourceCardNumber")
        @Expose
        var purseSourceCardNumber: String? = null

        @SerializedName("purseSourceCardReferenceID")
        @Expose
        var purseSourceCardReferenceID: String? = null

        @SerializedName("followMeCardNo")
        @Expose
        var followMeCardNo: String? = null

        @SerializedName("followMeCardReferenceID")
        @Expose
        var followMeCardReferenceID: String? = null

        @SerializedName("sourceCardNo")
        @Expose
        var sourceCardNo: String? = null

        @SerializedName("sourceCardReferenceNo")
        @Expose
        var sourceCardReferenceNo: String? = null

        @SerializedName("cardProgramID")
        @Expose
        var cardProgramID: String? = null

        @SerializedName("programAbbreviation")
        @Expose
        var programAbbreviation: String? = null

        @SerializedName("cardDesignId")
        @Expose
        var cardDesignId: String? = null

        @SerializedName("cardLogoId")
        @Expose
        var cardLogoId: String? = null

        @SerializedName("accountType")
        @Expose
        var accountType: Int = 0

        @SerializedName("accountTypeSpecified")
        @Expose
        var accountTypeSpecified = false

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null

        @SerializedName("middleName")
        @Expose
        var middleName: String? = null

        @SerializedName("lastName")
        @Expose
        var lastName: String? = null

        @SerializedName("memberNumber")
        @Expose
        var memberNumber: String? = null

        @SerializedName("lastDepositDate")
        @Expose
        var lastDepositDate: String? = null

        @SerializedName("lastDepositAmount")
        @Expose
        var lastDepositAmount: String? = null

        @SerializedName("expiryDate")
        @Expose
        var expiryDate: String? = null

        @SerializedName("statusCode")
        @Expose
        var statusCode: String? = null

        @SerializedName("isRegistered")
        @Expose
        var isRegistered: Int = 0

        @SerializedName("isRegisteredSpecified")
        @Expose
        var isRegisteredSpecified = false

        @SerializedName("balance")
        @Expose
        var balance: Double = 0.0

        @SerializedName("ledgerBalance")
        @Expose
        var ledgerBalance: Double = 0.0

        @SerializedName("ledgerBalanceSpecified")
        @Expose
        var ledgerBalanceSpecified = false

        @SerializedName("daysOfDueDate")
        @Expose
        var daysOfDueDate: Int = 0

        @SerializedName("daysOfDueDateSpecified")
        @Expose
        var daysOfDueDateSpecified = false

        @SerializedName("currencyCode")
        @Expose
        var currencyCode: String? = null

        @SerializedName("billingCurrencyCode")
        @Expose
        var billingCurrencyCode: String? = null

        @SerializedName("allocatedCreditLimit")
        @Expose
        var allocatedCreditLimit: Double = 0.0

        @SerializedName("allocatedCreditLimitSpecified")
        @Expose
        var allocatedCreditLimitSpecified = false

        @SerializedName("outstandingBalance")
        @Expose
        var outstandingBalance: Double = 0.0

        @SerializedName("outstandingBalanceSpecified")
        @Expose
        var outstandingBalanceSpecified = false

        @SerializedName("outstandingLedgerBalance")
        @Expose
        var outstandingLedgerBalance: Double = 0.0

        @SerializedName("outstandingLedgerBalanceSpecified")
        @Expose
        var outstandingLedgerBalanceSpecified = false

        @SerializedName("cashAdvanceBalance")
        @Expose
        var cashAdvanceBalance: Double = 0.0

        @SerializedName("cashAdvanceBalanceSpecified")
        @Expose
        var cashAdvanceBalanceSpecified = false

        @SerializedName("cashAdvanceLedgerBalance")
        @Expose
        var cashAdvanceLedgerBalance: Double = 0.0

        @SerializedName("cashAdvanceLedgerBalanceSpecified")
        @Expose
        var cashAdvanceLedgerBalanceSpecified = false

        @SerializedName("availableCreditLimit")
        @Expose
        var availableCreditLimit: Double = 0.0

        @SerializedName("availableCreditLimitSpecified")
        @Expose
        var availableCreditLimitSpecified = false

        @SerializedName("availableLegderCreditLimit")
        @Expose
        var availableLegderCreditLimit: Double = 0.0

        @SerializedName("availableLegderCreditLimitSpecified")
        @Expose
        var availableLegderCreditLimitSpecified = false

        @SerializedName("availableCashAdvanceLimit")
        @Expose
        var availableCashAdvanceLimit: Double = 0.0

        @SerializedName("availableCashAdvanceLimitSpecified")
        @Expose
        var availableCashAdvanceLimitSpecified = false

        @SerializedName("ledgerCashAdvanceCreditLimit")
        @Expose
        var ledgerCashAdvanceCreditLimit: Double = 0.0

        @SerializedName("ledgerCashAdvanceCreditLimitSpecified")
        @Expose
        var ledgerCashAdvanceCreditLimitSpecified = false

        @SerializedName("pendingTransactionsBalance")
        @Expose
        var pendingTransactionsBalance: Double = 0.0

        @SerializedName("pendingTransactionsBalanceSpecified")
        @Expose
        var pendingTransactionsBalanceSpecified = false

        @SerializedName("allocatedCashAdvanceLimit")
        @Expose
        var allocatedCashAdvanceLimit: Double = 0.0

        @SerializedName("allocatedCashAdvanceLimitSpecified")
        @Expose
        var allocatedCashAdvanceLimitSpecified = false

        @SerializedName("stmtDayOfMonth")
        @Expose
        var stmtDayOfMonth: String? = null

        @SerializedName("stakeholderId")
        @Expose
        var stakeholderId: String? = null

        @SerializedName("achProcessingDays")
        @Expose
        var achProcessingDays: String? = null

        @SerializedName("purchaseAPR")
        @Expose
        var purchaseAPR: Double = 0.0

        @SerializedName("purchaseAPRSpecified")
        @Expose
        var purchaseAPRSpecified = false

        @SerializedName("cashAdvanceAPR")
        @Expose
        var cashAdvanceAPR: Double = 0.0

        @SerializedName("cashAdvanceAPRSpecified")
        @Expose
        var cashAdvanceAPRSpecified = false

        @SerializedName("statementDate")
        @Expose
        var statementDate: String? = null

        @SerializedName("statementDateSpecified")
        @Expose
        var statementDateSpecified = false

        @SerializedName("deliveryMethod")
        @Expose
        var deliveryMethod: String? = null

        @SerializedName("delinquentTotalDays")
        @Expose
        var delinquentTotalDays: String? = null

        @SerializedName("delinquentDueAmount")
        @Expose
        var delinquentDueAmount: Double = 0.0

        @SerializedName("delinquentDueAmountSpecified")
        @Expose
        var delinquentDueAmountSpecified = false

        var isSelected = false
        var type  = 0
    }
}