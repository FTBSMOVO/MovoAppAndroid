package com.movocash.movo.data.model.responsemodel

import com.movocash.movo.data.model.responsemodel.GetMyPayeeResponseModel.Payee
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetSearchedPayeeResponseModel {

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

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("noOfRecords")
        @Expose
        var noOfRecords: Long = 0

        @SerializedName("noOfRecordsSpecified")
        @Expose
        var noOfRecordsSpecified = false

        @SerializedName("payees")
        @Expose
        var payees: ArrayList<Payee>? = null
    }
}