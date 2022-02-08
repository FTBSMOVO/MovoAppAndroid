package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PaymentResponseModel {
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

        @SerializedName("balance")
        @Expose
        var balance: Double = 0.0

        @SerializedName("balanceSpecified")
        @Expose
        var balanceSpecified = false

        @SerializedName("billPaymentTransId")
        @Expose
        var billPaymentTransId: String? = null

        @SerializedName("billPaymentProcessingDays")
        @Expose
        var billPaymentProcessingDays: String? = null

        @SerializedName("businessDate")
        @Expose
        var businessDate: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null
    }
}