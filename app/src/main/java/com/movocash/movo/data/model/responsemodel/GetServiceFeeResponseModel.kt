package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetServiceFeeResponseModel {
    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

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

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("cardNumber")
        @Expose
        var cardNumber: String? = null

        @SerializedName("referenceID")
        @Expose
        var referenceID: String? = null

        @SerializedName("cardProgramID")
        @Expose
        var cardProgramID: String? = null

        @SerializedName("expDate")
        @Expose
        var expDate: String? = null

        @SerializedName("statusCode")
        @Expose
        var statusCode: String? = null

        @SerializedName("maxLoadableAmount")
        @Expose
        var maxLoadableAmount: String? = null

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null

        @SerializedName("middleName")
        @Expose
        var middleName: String? = null

        @SerializedName("lastName")
        @Expose
        var lastName: String? = null

        @SerializedName("address1")
        @Expose
        var address1: String? = null

        @SerializedName("address2")
        @Expose
        var address2: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("stateCode")
        @Expose
        var stateCode: String? = null

        @SerializedName("postalCode")
        @Expose
        var postalCode: String? = null

        @SerializedName("country")
        @Expose
        var country: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("residentialPhone")
        @Expose
        var residentialPhone: String? = null

        @SerializedName("cellNumber")
        @Expose
        var cellNumber: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("gender")
        @Expose
        var gender: Long? = null

        @SerializedName("genderSpecified")
        @Expose
        var genderSpecified: Boolean? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null

        @SerializedName("minLoadAmount")
        @Expose
        var minLoadAmount: String? = null

        @SerializedName("requestedServiceFee")
        @Expose
        var requestedServiceFee: String = ""

        @SerializedName("restrictionName")
        @Expose
        var restrictionName: String? = null

        @SerializedName("paramValueType")
        @Expose
        var paramValueType: String? = null

        @SerializedName("paramValue")
        @Expose
        var paramValue: String? = null

        @SerializedName("paramMaxValue")
        @Expose
        var paramMaxValue: String? = null

        @SerializedName("paramMinValue")
        @Expose
        var paramMinValue: String? = null

        @SerializedName("frequency")
        @Expose
        var frequency: Long? = null

        @SerializedName("frequencySpecified")
        @Expose
        var frequencySpecified: Boolean? = null

        @SerializedName("minDebitAmount")
        @Expose
        var minDebitAmount: String? = null

        @SerializedName("maxDebitAmount")
        @Expose
        var maxDebitAmount: String? = null

        @SerializedName("fraudParams")
        @Expose
        var fraudParams: List<FraudParam>? = null
    }

    class FraudParam{
        @SerializedName("paramName")
        @Expose
        var paramName: String? = null

        @SerializedName("paramValueType")
        @Expose
        var paramValueType: String? = null

        @SerializedName("paramValue")
        @Expose
        var paramValue: String? = null

        @SerializedName("paramMinValue")
        @Expose
        var paramMinValue: String? = null

        @SerializedName("paramMaxValue")
        @Expose
        var paramMaxValue: String? = null

        @SerializedName("paramFrequency")
        @Expose
        var paramFrequency: Long? = null

        @SerializedName("paramFrequencySpecified")
        @Expose
        var paramFrequencySpecified: Boolean? = null
    }
}