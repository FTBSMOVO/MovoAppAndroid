package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReissueCardModel {


        @SerializedName("isError")
        @Expose
        var isError: Boolean? = null

        @SerializedName("messages")
        @Expose
        var messages: String? = null

        @SerializedName("data")
        @Expose
        var data: Data? = null


}

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
    var ledgerBalance: Int? = null

    @SerializedName("ledgerBalanceSpecified")
    @Expose
    var ledgerBalanceSpecified: Boolean? = null

    @SerializedName("amountRequested")
    @Expose
    var amountRequested: Int? = null

    @SerializedName("amountRequestedSpecified")
    @Expose
    var amountRequestedSpecified: Boolean? = null

    @SerializedName("amountProcessed")
    @Expose
    var amountProcessed: Int? = null

    @SerializedName("amountProcessedSpecified")
    @Expose
    var amountProcessedSpecified: Boolean? = null

    @SerializedName("partialFundsFlag")
    @Expose
    var partialFundsFlag: Int? = null

    @SerializedName("partialFundsFlagSpecified")
    @Expose
    var partialFundsFlagSpecified: Boolean? = null

    @SerializedName("partialPointsFlag")
    @Expose
    var partialPointsFlag: Int? = null

    @SerializedName("partialPointsFlagSpecified")
    @Expose
    var partialPointsFlagSpecified: Boolean? = null

    @SerializedName("pointsRequested")
    @Expose
    var pointsRequested: Int? = null

    @SerializedName("pointsRequestedSpecified")
    @Expose
    var pointsRequestedSpecified: Boolean? = null

    @SerializedName("pointsRedeemed")
    @Expose
    var pointsRedeemed: Int? = null

    @SerializedName("pointsRedeemedSpecified")
    @Expose
    var pointsRedeemedSpecified: Boolean? = null

    @SerializedName("pointsBalance")
    @Expose
    var pointsBalance: Int? = null

    @SerializedName("pointsBalanceSpecified")
    @Expose
    var pointsBalanceSpecified: Boolean? = null

    @SerializedName("pointsExchangeRate")
    @Expose
    var pointsExchangeRate: Int? = null

    @SerializedName("pointsExchangeRateSpecified")
    @Expose
    var pointsExchangeRateSpecified: Boolean? = null

    @SerializedName("pointsAmount")
    @Expose
    var pointsAmount: Int? = null

    @SerializedName("pointsAmountSpecified")
    @Expose
    var pointsAmountSpecified: Boolean? = null

    @SerializedName("isRegistered")
    @Expose
    var isRegistered: Int? = null

    @SerializedName("isRegisteredSpecified")
    @Expose
    var isRegisteredSpecified: Boolean? = null

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
    var isBadPINTriesExceeded: Int? = null

    @SerializedName("isBadPINTriesExceededSpecified")
    @Expose
    var isBadPINTriesExceededSpecified: Boolean? = null

    @SerializedName("transitionFlag")
    @Expose
    var transitionFlag: Int? = null

    @SerializedName("transitionFlagSpecified")
    @Expose
    var transitionFlagSpecified: Boolean? = null

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

class NewCardNumber {
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

class Offer {
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
    var couponValue: Int? = null

    @SerializedName("couponValueSpecified")
    @Expose
    var couponValueSpecified: Boolean? = null

    @SerializedName("availableCouponValue")
    @Expose
    var availableCouponValue: Int? = null

    @SerializedName("availableCouponValueSpecified")
    @Expose
    var availableCouponValueSpecified: Boolean? = null

    @SerializedName("couponValueRedeemed")
    @Expose
    var couponValueRedeemed: Int? = null

    @SerializedName("couponValueRedeemedSpecified")
    @Expose
    var couponValueRedeemedSpecified: Boolean? = null

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
    var minPurchaseAmount: Int? = null

    @SerializedName("minPurchaseAmountSpecified")
    @Expose
    var minPurchaseAmountSpecified: Boolean? = null

    @SerializedName("maxRedemptionCount")
    @Expose
    var maxRedemptionCount: String? = null

    @SerializedName("isPartialRedemption")
    @Expose
    var isPartialRedemption: Int? = null

    @SerializedName("isPartialRedemptionSpecified")
    @Expose
    var isPartialRedemptionSpecified: Boolean? = null

    @SerializedName("isMultiOffer")
    @Expose
    var isMultiOffer: Int? = null

    @SerializedName("isMultiOfferSpecified")
    @Expose
    var isMultiOfferSpecified: Boolean? = null

    @SerializedName("basis")
    @Expose
    var basis: Int? = null

    @SerializedName("basisSpecified")
    @Expose
    var basisSpecified: Boolean? = null

    @SerializedName("percentage")
    @Expose
    var percentage: Int? = null

    @SerializedName("percentageSpecified")
    @Expose
    var percentageSpecified: Boolean? = null

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

class Offers {
    @SerializedName("totalOffersRedeemed")
    @Expose
    var totalOffersRedeemed: Int? = null

    @SerializedName("totalOffersRedeemedSpecified")
    @Expose
    var totalOffersRedeemedSpecified: Boolean? = null

    @SerializedName("totalRedeemedAmount")
    @Expose
    var totalRedeemedAmount: Int? = null

    @SerializedName("totalRedeemedAmountSpecified")
    @Expose
    var totalRedeemedAmountSpecified: Boolean? = null

    @SerializedName("offer")
    @Expose
    var offer: List<Offer>? = null
}

class SharingCard {
    @SerializedName("sharingCardNumber")
    @Expose
    var sharingCardNumber: String? = null

    @SerializedName("sharingCardReferenceID")
    @Expose
    var sharingCardReferenceID: String? = null
}
class Status {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null
}

