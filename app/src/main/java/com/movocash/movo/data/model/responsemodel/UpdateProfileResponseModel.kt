package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.movocash.movo.data.model.responsemodel.ShareFundResponseModel.NewCardNumber
import com.movocash.movo.data.model.responsemodel.ShareFundResponseModel.SharingCard

class UpdateProfileResponseModel {
    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("responseCode")
        @Expose
        var responseCode: String? = null

        @SerializedName("responseDesc")
        @Expose
        var responseDesc: String? = null

        @SerializedName("cardNumber")
        @Expose
        var cardNumber: String? = null

        @SerializedName("cardReferenceID")
        @Expose
        var cardReferenceID: String? = null

        @SerializedName("newCardNumber")
        @Expose
        var newCardNumber: NewCardNumber? = null

        @SerializedName("newCardReferenceID")
        @Expose
        var newCardReferenceID: String? = null

        @SerializedName("upgradedCardNumber")
        @Expose
        var upgradedCardNumber: String? = null

        @SerializedName("upgradedCardReferenceID")
        @Expose
        var upgradedCardReferenceID: String? = null

        @SerializedName("sharingCards")
        @Expose
        var sharingCards: List<SharingCard>? = null

        @SerializedName("pin")
        @Expose
        var pin: String? = null

        @SerializedName("pinOffset")
        @Expose
        var pinOffset: String? = null

        @SerializedName("status")
        @Expose
        var status: ShareFundResponseModel.Status? = null

        @SerializedName("fromCardBalance")
        @Expose
        var fromCardBalance: String? = null

        @SerializedName("toCardBalance")
        @Expose
        var toCardBalance: String? = null

        @SerializedName("cvV2")
        @Expose
        var cvV2: String? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

        @SerializedName("ledgerBalance")
        @Expose
        var ledgerBalance: Long? = null

        @SerializedName("ledgerBalanceSpecified")
        @Expose
        var ledgerBalanceSpecified: Boolean = false

        @SerializedName("amountRequested")
        @Expose
        var amountRequested: Long? = null

        @SerializedName("amountRequestedSpecified")
        @Expose
        var amountRequestedSpecified: Boolean = false

        @SerializedName("amountProcessed")
        @Expose
        var amountProcessed: Long? = null

        @SerializedName("amountProcessedSpecified")
        @Expose
        var amountProcessedSpecified: Boolean = false

        @SerializedName("partialFundsFlag")
        @Expose
        var partialFundsFlag: Long? = null

        @SerializedName("partialFundsFlagSpecified")
        @Expose
        var partialFundsFlagSpecified: Boolean = false

        @SerializedName("partialPointsFlag")
        @Expose
        var partialPointsFlag: Long? = null

        @SerializedName("partialPointsFlagSpecified")
        @Expose
        var partialPointsFlagSpecified: Boolean = false

        @SerializedName("pointsRequested")
        @Expose
        var pointsRequested: Long? = null

        @SerializedName("pointsRequestedSpecified")
        @Expose
        var pointsRequestedSpecified: Boolean = false

        @SerializedName("pointsRedeemed")
        @Expose
        var pointsRedeemed: Long? = null

        @SerializedName("pointsRedeemedSpecified")
        @Expose
        var pointsRedeemedSpecified: Boolean = false

        @SerializedName("pointsBalance")
        @Expose
        var pointsBalance: Long? = null

        @SerializedName("pointsBalanceSpecified")
        @Expose
        var pointsBalanceSpecified: Boolean = false

        @SerializedName("pointsExchangeRate")
        @Expose
        var pointsExchangeRate: Long? = null

        @SerializedName("pointsExchangeRateSpecified")
        @Expose
        var pointsExchangeRateSpecified: Boolean = false

        @SerializedName("pointsAmount")
        @Expose
        var pointsAmount: Long? = null

        @SerializedName("pointsAmountSpecified")
        @Expose
        var pointsAmountSpecified: Boolean = false

        @SerializedName("isRegistered")
        @Expose
        var isRegistered: Long? = null

        @SerializedName("isRegisteredSpecified")
        @Expose
        var isRegisteredSpecified: Boolean = false

        @SerializedName("offers")
        @Expose
        var offers: CardBlockedResponseModel.Offers? = null

        @SerializedName("lastDepositDate")
        @Expose
        var lastDepositDate: String? = null

        @SerializedName("lastDepositAmount")
        @Expose
        var lastDepositAmount: String? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("clerkID")
        @Expose
        var clerkID: String? = null

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("cardProgramName")
        @Expose
        var cardProgramName: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null

        @SerializedName("referenceID")
        @Expose
        var referenceID: String? = null

        @SerializedName("expiryDate")
        @Expose
        var expiryDate: String? = null

        @SerializedName("batchReferenceID")
        @Expose
        var batchReferenceID: String? = null

        @SerializedName("fundsExpiryDate")
        @Expose
        var fundsExpiryDate: String? = null

        @SerializedName("isBadPINTriesExceeded")
        @Expose
        var isBadPINTriesExceeded: Long? = null

        @SerializedName("isBadPINTriesExceededSpecified")
        @Expose
        var isBadPINTriesExceededSpecified: Boolean = false

        @SerializedName("transitionFlag")
        @Expose
        var transitionFlag: Long? = null

        @SerializedName("transitionFlagSpecified")
        @Expose
        var transitionFlagSpecified: Boolean = false

        @SerializedName("newCardExpiryDate")
        @Expose
        var newCardExpiryDate: String? = null

        @SerializedName("newCardCVV2")
        @Expose
        var newCardCVV2: String? = null

        @SerializedName("nameOnCard")
        @Expose
        var nameOnCard: String? = null

        @SerializedName("virtualCardNumber")
        @Expose
        var virtualCardNumber: String? = null

        @SerializedName("maaSubUnSubRespDesc")
        @Expose
        var maaSubUnSubRespDesc: String? = null
    }
}