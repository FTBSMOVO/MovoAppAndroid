package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ShareFundResponseModel {
    @SerializedName("isError")
    @Expose
    var isError = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data{
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
        var status: Status? = null

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
        var ledgerBalance: Double = 0.0

        @SerializedName("ledgerBalanceSpecified")
        @Expose
        var ledgerBalanceSpecified = false

        @SerializedName("amountRequested")
        @Expose
        var amountRequested: Double = 0.0

        @SerializedName("amountRequestedSpecified")
        @Expose
        var amountRequestedSpecified = false

        @SerializedName("amountProcessed")
        @Expose
        var amountProcessed: Double = 0.0

        @SerializedName("amountProcessedSpecified")
        @Expose
        var amountProcessedSpecified = false

        @SerializedName("partialFundsFlag")
        @Expose
        var partialFundsFlag: Int = 0

        @SerializedName("partialFundsFlagSpecified")
        @Expose
        var partialFundsFlagSpecified = false

        @SerializedName("partialPointsFlag")
        @Expose
        var partialPointsFlag: Int = 0

        @SerializedName("partialPointsFlagSpecified")
        @Expose
        var partialPointsFlagSpecified = false

        @SerializedName("pointsRequested")
        @Expose
        var pointsRequested: Int = 0

        @SerializedName("pointsRequestedSpecified")
        @Expose
        var pointsRequestedSpecified = false

        @SerializedName("pointsRedeemed")
        @Expose
        var pointsRedeemed: Int = 0

        @SerializedName("pointsRedeemedSpecified")
        @Expose
        var pointsRedeemedSpecified = false

        @SerializedName("pointsBalance")
        @Expose
        var pointsBalance: Double = 0.0

        @SerializedName("pointsBalanceSpecified")
        @Expose
        var pointsBalanceSpecified = false

        @SerializedName("pointsExchangeRate")
        @Expose
        var pointsExchangeRate: Double = 0.0

        @SerializedName("pointsExchangeRateSpecified")
        @Expose
        var pointsExchangeRateSpecified = false

        @SerializedName("pointsAmount")
        @Expose
        var pointsAmount: Double = 0.0

        @SerializedName("pointsAmountSpecified")
        @Expose
        var pointsAmountSpecified = false

        @SerializedName("isRegistered")
        @Expose
        var isRegistered: Int = 0

        @SerializedName("isRegisteredSpecified")
        @Expose
        var isRegisteredSpecified = false

        @SerializedName("offers")
        @Expose
        var offers: Offers? = null

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
        var isBadPINTriesExceeded: Int = 0

        @SerializedName("isBadPINTriesExceededSpecified")
        @Expose
        var isBadPINTriesExceededSpecified = false

        @SerializedName("transitionFlag")
        @Expose
        var transitionFlag: Int = 0

        @SerializedName("transitionFlagSpecified")
        @Expose
        var transitionFlagSpecified = false

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

    class Offers{
        @SerializedName("icouponProgramId")
        @Expose
        var icouponProgramId: String? = null

        @SerializedName("couponProgramDesc")
        @Expose
        var couponProgramDesc: String? = null

        @SerializedName("couponNo")
        @Expose
        var couponNo: String? = null

        @SerializedName("couponValue")
        @Expose
        var couponValue: Int = 0

        @SerializedName("couponValueSpecified")
        @Expose
        var couponValueSpecified = false

        @SerializedName("availableCouponValue")
        @Expose
        var availableCouponValue: Double = 0.0

        @SerializedName("availableCouponValueSpecified")
        @Expose
        var availableCouponValueSpecified = false

        @SerializedName("couponValueRedeemed")
        @Expose
        var couponValueRedeemed: Double = 0.0

        @SerializedName("couponValueRedeemedSpecified")
        @Expose
        var couponValueRedeemedSpecified = false

        @SerializedName("couponStatus")
        @Expose
        var couponStatus: String? = null

        @SerializedName("couponType")
        @Expose
        var couponType: String? = null

        @SerializedName("packageID")
        @Expose
        var packageID: String? = null

        @SerializedName("packageName")
        @Expose
        var packageName: String? = null

        @SerializedName("packageData")
        @Expose
        var packageData: String? = null

        @SerializedName("acquirerID")
        @Expose
        var acquirerID: String? = null

        @SerializedName("merchantID")
        @Expose
        var merchantID: String? = null

        @SerializedName("minPurchaseAmount")
        @Expose
        var minPurchaseAmount: Double = 0.0

        @SerializedName("minPurchaseAmountSpecified")
        @Expose
        var minPurchaseAmountSpecified = false

        @SerializedName("maxRedemptionCount")
        @Expose
        var maxRedemptionCount: String? = null

        @SerializedName("isPartialRedemption")
        @Expose
        var isPartialRedemption: Int = 0

        @SerializedName("isPartialRedemptionSpecified")
        @Expose
        var isPartialRedemptionSpecified = false

        @SerializedName("isMultiOffer")
        @Expose
        var isMultiOffer: Int = 0

        @SerializedName("isMultiOfferSpecified")
        @Expose
        var isMultiOfferSpecified = false

        @SerializedName("basis")
        @Expose
        var basis: Int = 0

        @SerializedName("basisSpecified")
        @Expose
        var basisSpecified = false

        @SerializedName("percentage")
        @Expose
        var percentage: Int = 0

        @SerializedName("percentageSpecified")
        @Expose
        var percentageSpecified = false

        @SerializedName("effectiveDateFrom")
        @Expose
        var effectiveDateFrom: String? = null

        @SerializedName("expirationPeriodType")
        @Expose
        var expirationPeriodType: String? = null

        @SerializedName("expirationPeriodValue")
        @Expose
        var expirationPeriodValue: String? = null

        @SerializedName("startTime")
        @Expose
        var startTime: String? = null

        @SerializedName("endTime")
        @Expose
        var endTime: String? = null

        @SerializedName("daysOfWeek")
        @Expose
        var daysOfWeek: String? = null

        @SerializedName("daysOfMonth")
        @Expose
        var daysOfMonth: String? = null

        @SerializedName("monthsOfYear")
        @Expose
        var monthsOfYear: String? = null
    }

    class Status{
        @SerializedName("code")
        @Expose
        var code: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null
    }

    class NewCardNumber{
        @SerializedName("number")
        @Expose
        var number: String? = null

        @SerializedName("accountNumber")
        @Expose
        var accountNumber: String? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

        @SerializedName("expiryDate")
        @Expose
        var expiryDate: String? = null
    }

    class SharingCard{
        @SerializedName("sharingCardNumber")
        @Expose
        var sharingCardNumber: String? = null

        @SerializedName("sharingCardReferenceID")
        @Expose
        var sharingCardReferenceID: String? = null
    }
}