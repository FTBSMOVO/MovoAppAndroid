package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetShareHistoryResponseModel {
    @SerializedName("isError")
    @Expose
    var isError = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var historyList: ArrayList<HistoryItem>? = null

    class HistoryItem{
        @SerializedName("createdOn")
        @Expose
        var createdOn: String? = null

        @SerializedName("label")
        @Expose
        var label: String? = null

        @SerializedName("amount")
        @Expose
        var amount: Double = 0.0

        @SerializedName("payTo")
        @Expose
        var payTo: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("comments")
        @Expose
        var comments: String? = null
    }
}