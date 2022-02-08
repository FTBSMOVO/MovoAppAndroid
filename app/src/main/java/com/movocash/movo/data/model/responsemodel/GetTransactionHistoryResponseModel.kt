package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetTransactionHistoryResponseModel {
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

        @SerializedName("statement")
        @Expose
        var statement: Statement? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

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

        @SerializedName("fee")
        @Expose
        var fee: String? = null

        @SerializedName("businessDate")
        @Expose
        var businessDate: String? = null

        @SerializedName("cardProgramName")
        @Expose
        var cardProgramName: String? = null
    }

    class Statement{
        @SerializedName("currentBalance")
        @Expose
        var currentBalance: String? = null

        @SerializedName("transactions")
        @Expose
        var transactions: ArrayList<Transaction>? = null
    }

    class Transaction{
        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("traceAuditNo")
        @Expose
        var traceAuditNo: String? = null

        @SerializedName("originalTraceAuditNo")
        @Expose
        var originalTraceAuditNo: String? = null

        @SerializedName("additional")
        @Expose
        var additional: Additional? = null

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

        @SerializedName("availableBalance")
        @Expose
        var availableBalance: String? = null

        @SerializedName("remainingBalance")
        @Expose
        var remainingBalance: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Double = 0.0

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("messageTypeIdentifier")
        @Expose
        var messageTypeIdentifier: String? = null

        @SerializedName("billed")
        @Expose
        var billed: Int = 0

        @SerializedName("billedSpecified")
        @Expose
        var billedSpecified = false

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

        @SerializedName("panSequenceNo")
        @Expose
        var panSequenceNo: String? = null

        @SerializedName("transactionExpiryDate")
        @Expose
        var transactionExpiryDate: String? = null

        @SerializedName("disputeType")
        @Expose
        var disputeType: Int = 0

        @SerializedName("disputeTypeSpecified")
        @Expose
        var disputeTypeSpecified = false

        @SerializedName("disputeStatus")
        @Expose
        var disputeStatus: Int = 0

        @SerializedName("disputeStatusSpecified")
        @Expose
        var disputeStatusSpecified = false

        @SerializedName("disputeAmount")
        @Expose
        var disputeAmount: Double = 0.0

        @SerializedName("disputeAmountSpecified")
        @Expose
        var disputeAmountSpecified = false

        @SerializedName("disputeDate")
        @Expose
        var disputeDate: String? = null

        @SerializedName("disputeDateSpecified")
        @Expose
        var disputeDateSpecified = false

        @SerializedName("isPaymentTrans")
        @Expose
        var isPaymentTrans: String? = null

        @SerializedName("walletTokenNo")
        @Expose
        var walletTokenNo: String? = null

        @SerializedName("walletTokenRequesterId")
        @Expose
        var walletTokenRequesterId: String? = null

        @SerializedName("walletTokenType")
        @Expose
        var walletTokenType: String? = null

        @SerializedName("walletTokenExpiry")
        @Expose
        var walletTokenExpiry: String? = null

        @SerializedName("authorizationCode")
        @Expose
        var authorizationCode: String? = null

        @SerializedName("additionalAmount")
        @Expose
        var additionalAmount: String? = null
    }

    class Additional{
        @SerializedName("data1")
        @Expose
        var data1: String? = null

        @SerializedName("data2")
        @Expose
        var data2: String? = null

        @SerializedName("data3")
        @Expose
        var data3: String? = null
    }
}