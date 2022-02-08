package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class getUserScheduleReloadResponseModel {
    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var userData: UserData? = null

    class UserData{
        @SerializedName("id")
        @Expose
        var id: String? = null


        @SerializedName("cardReferenceId")
        @Expose
        var cardReferenceId: String? = null

        @SerializedName("isEnabled")
        @Expose
        var isEnabled: Boolean? = null

        @SerializedName("serviceProvider")
        @Expose
        var serviceProvider: String? = null

        @SerializedName("paymentAmount")
        @Expose
        var paymentAmount: Double? = null

        @SerializedName("paymentDueDate")
        @Expose
        var paymentDueDate: String? = null

        @SerializedName("autoReloadDate")
        @Expose
        var autoReloadDate: String? = null

        @SerializedName("nextPaymentDueDate")
        @Expose
        var nextPaymentDueDate: String? = null

        @SerializedName("nextAutoReloadDate")
        @Expose
        var nextAutoReloadDate: String? = null

        @SerializedName("frequencyId")
        @Expose
        var frequencyId: Int? = null
    }
}