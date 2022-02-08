package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetCardAuthResponseModel {
    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var cardData: CardData? = null

    class CardData{
        @SerializedName("cvV2")
        @Expose
        var cvV2: String? = null

        @SerializedName("balance")
        @Expose
        var balance: Double = 0.0

        @SerializedName("expiryDate")
        @Expose
        var expiryDate: String? = null
    }
}