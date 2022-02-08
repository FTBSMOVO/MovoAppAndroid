package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetMiniStatementResponseModel {

    @SerializedName("isError")
    @Expose
    var isError = false

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

        @SerializedName("statement")
        @Expose
        var statement: Statement? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("clerkId")
        @Expose
        var clerkId: String? = null

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: Double = 0.0

        @SerializedName("feeSpecified")
        @Expose
        var feeSpecified = false
    }

    class Statement {
        @SerializedName("transactions")
        @Expose
        var transactions: ArrayList<Transaction>? = null
    }

    class Transaction {
        @SerializedName("cardProgramID")
        @Expose
        var cardProgramID: String? = null

        @SerializedName("programAbbreviation")
        @Expose
        var programAbbreviation: String? = null

        @SerializedName("cardReferenceID")
        @Expose
        var cardReferenceID: String? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("accountNumber")
        @Expose
        var accountNumber: String? = null

        @SerializedName("transTypeId")
        @Expose
        var transTypeId: String? = null

        @SerializedName("transDate")
        @Expose
        var transDate: String? = null

        @SerializedName("transDateSpecified")
        @Expose
        var transDateSpecified = false

        @SerializedName("businessDate")
        @Expose
        var businessDate: String? = null

        @SerializedName("acceptorNameAndLocation")
        @Expose
        var acceptorNameAndLocation: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Double = 0.0

        @SerializedName("amountSpecified")
        @Expose
        var amountSpecified = false

        @SerializedName("currencyCode")
        @Expose
        var currencyCode: String? = null

        @SerializedName("amountRequested")
        @Expose
        var amountRequested: Double = 0.0

        @SerializedName("amountRequestedSpecified")
        @Expose
        var amountRequestedSpecified = false

        @SerializedName("requestedCurrencyCode")
        @Expose
        var requestedCurrencyCode: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("messageTypeIdentifier")
        @Expose
        var messageTypeIdentifier: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("cardAcceptorId")
        @Expose
        var cardAcceptorId: String? = null

        @SerializedName("mcc")
        @Expose
        var mcc: String? = null

        @SerializedName("deviceId")
        @Expose
        var deviceId: String? = null

        @SerializedName("deviceType")
        @Expose
        var deviceType: String? = null

        @SerializedName("merchantName")
        @Expose
        var merchantName: String? = null

        @SerializedName("transactionExpiryDate")
        @Expose
        var transactionExpiryDate: String? = null
    }
}