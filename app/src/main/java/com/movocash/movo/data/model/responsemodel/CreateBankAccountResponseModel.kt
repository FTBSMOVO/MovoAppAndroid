package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CreateBankAccountResponseModel {
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

        @SerializedName("accountSrNo")
        @Expose
        var accountSrNo: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("b2CMinTransferDays")
        @Expose
        var b2CMinTransferDays: String? = null

        @SerializedName("b2CMaxTransferDays")
        @Expose
        var b2CMaxTransferDays: String? = null

        @SerializedName("c2BMinTransferDays")
        @Expose
        var c2BMinTransferDays: String? = null

        @SerializedName("c2BMaxTransferDays")
        @Expose
        var c2BMaxTransferDays: String? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null
    }
}