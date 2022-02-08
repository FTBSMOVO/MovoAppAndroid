package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PayeeResponseModel {
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

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null
    }
}