package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetMyProfileResponseModel {
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

        @SerializedName("cardNumber")
        @Expose
        var cardNumber: String? = null

        @SerializedName("status")
        @Expose
        var status: ShareFundResponseModel.Status? = null

        @SerializedName("pinOffset")
        @Expose
        var pinOffset: String? = null

        @SerializedName("balance")
        @Expose
        var balance: String? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("clerkID")
        @Expose
        var clerkID: String? = null

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null

        @SerializedName("referenceID")
        @Expose
        var referenceID: String? = null

        @SerializedName("pseudoDDANumber")
        @Expose
        var pseudoDDANumber: String? = null

        @SerializedName("abaRoutingNumber")
        @Expose
        var abaRoutingNumber: String? = null

        @SerializedName("lastDepositDate")
        @Expose
        var lastDepositDate: String? = null

        @SerializedName("lastDepositAmount")
        @Expose
        var lastDepositAmount: String? = null

        @SerializedName("firstName")
        @Expose
        var firstName: String? = null

        @SerializedName("middleName")
        @Expose
        var middleName: String? = null

        @SerializedName("lastName")
        @Expose
        var lastName: String? = null

        @SerializedName("suffix")
        @Expose
        var suffix: String? = null

        @SerializedName("address1")
        @Expose
        var address1: String? = null

        @SerializedName("address2")
        @Expose
        var address2: String? = null

        @SerializedName("address3")
        @Expose
        var address3: String? = null

        @SerializedName("address4")
        @Expose
        var address4: String? = null

        @SerializedName("address5")
        @Expose
        var address5: String? = null

        @SerializedName("dob")
        @Expose
        var dob: String? = null

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

        @SerializedName("billingAddress1")
        @Expose
        var billingAddress1: String? = null

        @SerializedName("billingAddress2")
        @Expose
        var billingAddress2: String? = null

        @SerializedName("billingAddress3")
        @Expose
        var billingAddress3: String? = null

        @SerializedName("billingAddress4")
        @Expose
        var billingAddress4: String? = null

        @SerializedName("billingAddress5")
        @Expose
        var billingAddress5: String? = null

        @SerializedName("billingCity")
        @Expose
        var billingCity: String? = null

        @SerializedName("billingStateCode")
        @Expose
        var billingStateCode: String? = null

        @SerializedName("billingPostalCode")
        @Expose
        var billingPostalCode: String? = null

        @SerializedName("billingCountry")
        @Expose
        var billingCountry: String? = null

        @SerializedName("expDate")
        @Expose
        var expDate: String? = null

        @SerializedName("cardProgramID")
        @Expose
        var cardProgramID: String? = null

        @SerializedName("cardType")
        @Expose
        var cardType: String? = null

        @SerializedName("fulfillmentHouse")
        @Expose
        var fulfillmentHouse: String? = null

        @SerializedName("statusCode")
        @Expose
        var statusCode: String? = null

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
        var genderSpecified: Boolean = false

        @SerializedName("ssn")
        @Expose
        var ssn: String? = null

        @SerializedName("drivingLicense")
        @Expose
        var drivingLicense: String? = null

        @SerializedName("drivingLicenseState")
        @Expose
        var drivingLicenseState: String? = null

        @SerializedName("foreignID")
        @Expose
        var foreignID: String? = null

        @SerializedName("foreignIDType")
        @Expose
        var foreignIDType: String? = null

        @SerializedName("foreignCountryCode")
        @Expose
        var foreignCountryCode: String? = null

        @SerializedName("fundsExpiryDate")
        @Expose
        var fundsExpiryDate: String? = null

        @SerializedName("transitionFlag")
        @Expose
        var transitionFlag: Long? = null

        @SerializedName("transitionFlagSpecified")
        @Expose
        var transitionFlagSpecified: Boolean = false

        @SerializedName("citizenCountry")
        @Expose
        var citizenCountry: String? = null

        @SerializedName("occupation")
        @Expose
        var occupation: String? = null

        @SerializedName("memberNumber")
        @Expose
        var memberNumber: String? = null

        @SerializedName("cipCleared")
        @Expose
        var cipCleared: Long? = null

        @SerializedName("cipClearedSpecified")
        @Expose
        var cipClearedSpecified: Boolean = false

        @SerializedName("nameofEmployer")
        @Expose
        var nameofEmployer: String? = null

        @SerializedName("employmentStatus")
        @Expose
        var employmentStatus: String? = null

        @SerializedName("sourceCardNo")
        @Expose
        var sourceCardNo: String? = null

        @SerializedName("sourceCardReferenceNo")
        @Expose
        var sourceCardReferenceNo: String? = null

        @SerializedName("stakeholderId")
        @Expose
        var stakeholderId: String? = null

        @SerializedName("deliveryMethod")
        @Expose
        var deliveryMethod: Long? = null

        @SerializedName("deliveryMethodSpecified")
        @Expose
        var deliveryMethodSpecified: Boolean = false

        @SerializedName("achProcessingDays")
        @Expose
        var achProcessingDays: String? = null
    }
}