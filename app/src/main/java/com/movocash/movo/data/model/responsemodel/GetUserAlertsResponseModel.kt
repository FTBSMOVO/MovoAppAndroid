package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetUserAlertsResponseModel {

    @SerializedName("isError")
    @Expose
    var isError = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var mainList: ArrayList<MainObj>? = null

    class MainObj{
        @SerializedName("alertId")
        @Expose
        var alertId = 0

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("alertTypeId")
        @Expose
        var alertTypeId: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("isMultiple")
        @Expose
        var isMultiple: Boolean = false

        @SerializedName("isOperator")
        @Expose
        var isOperator: Boolean = false

        @SerializedName("alerts")
        @Expose
        var alertsList: ArrayList<Alert>? = null
    }

    class Alert{
        @SerializedName("alertId")
        @Expose
        var alertId = 0

        @SerializedName("alertTypeId")
        @Expose
        var alertTypeId: String? = null

        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("operatorTypeId")
        @Expose
        var operatorTypeId = 0

        @SerializedName("amount")
        @Expose
        var amount = 0.0

        @SerializedName("sms")
        @Expose
        var sms: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("mobilePush")
        @Expose
        var mobilePush = false
    }
}