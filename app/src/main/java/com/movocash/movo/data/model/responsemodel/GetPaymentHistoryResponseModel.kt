package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetPaymentHistoryResponseModel {
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

        @SerializedName("transactions")
        @Expose
        var transactions: ArrayList<Transaction>? = null

        @SerializedName("balance")
        @Expose
        var balance: Double = 0.0

        @SerializedName("balanceSpecified")
        @Expose
        var balanceSpecified = false

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null
    }

    class Transaction {
        @SerializedName("billPaymentTransId")
        @Expose
        var billPaymentTransId: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Double = 0.0

        @SerializedName("amountSpecified")
        @Expose
        var amountSpecified = false

        @SerializedName("scheduledDate")
        @Expose
        var scheduledDate: String? = null

        @SerializedName("scheduledDateSpecified")
        @Expose
        var scheduledDateSpecified = false

        @SerializedName("payeeSerialNo")
        @Expose
        var payeeSerialNo: String? = null

        @SerializedName("payeeName")
        @Expose
        var payeeName: String? = null

        @SerializedName("payeeAddress")
        @Expose
        var payeeAddress: String? = null

        @SerializedName("payeeCity")
        @Expose
        var payeeCity: String? = null

        @SerializedName("payeeState")
        @Expose
        var payeeState: String? = null

        @SerializedName("payeeZip")
        @Expose
        var payeeZip: String? = null

        @SerializedName("payeeNickname")
        @Expose
        var payeeNickname: String? = null

        @SerializedName("payeeAccountNumber")
        @Expose
        var payeeAccountNumber: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

    }
}