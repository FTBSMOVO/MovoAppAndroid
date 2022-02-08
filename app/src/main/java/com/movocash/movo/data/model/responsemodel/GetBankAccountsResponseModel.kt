package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetBankAccountsResponseModel {
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

        @SerializedName("customerId")
        @Expose
        var customerId: String? = null

        @SerializedName("accounts")
        @Expose
        var accounts: ArrayList<Account>? = null

        @SerializedName("noOfRecords")
        @Expose
        var noOfRecords: Int = 0

        @SerializedName("noOfRecordsSpecified")
        @Expose
        var noOfRecordsSpecified = false

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

    class Account {
        @SerializedName("accountSrNo")
        @Expose
        var accountSrNo: String? = null

        @SerializedName("accountNumber")
        @Expose
        var accountNumber: String? = null

        @SerializedName("accountTitle")
        @Expose
        var accountTitle: String? = null

        @SerializedName("accountType")
        @Expose
        var accountType: Int = 0

        @SerializedName("accountTypeSpecified")
        @Expose
        var accountTypeSpecified = false

        @SerializedName("accountNickname")
        @Expose
        var accountNickname: String? = null

        @SerializedName("achType")
        @Expose
        var achType: Int = 0

        @SerializedName("achTypeSpecified")
        @Expose
        var achTypeSpecified = false

        @SerializedName("bankName")
        @Expose
        var bankName: String? = null

        @SerializedName("routingNumber")
        @Expose
        var routingNumber: String? = null

        @SerializedName("comments")
        @Expose
        var comments: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("failedRegistrationTries")
        @Expose
        var failedRegistrationTries: String? = null

        @SerializedName("createdAt")
        @Expose
        var createdAt: String? = null

        @SerializedName("registrationExpiresAt")
        @Expose
        var registrationExpiresAt: String? = null

        @SerializedName("editAllowed")
        @Expose
        var editAllowed: Int = 0

        @SerializedName("editAllowedSpecified")
        @Expose
        var editAllowedSpecified = false

        @SerializedName("deleteAllowed")
        @Expose
        var deleteAllowed: Int = 0

        @SerializedName("deleteAllowedSpecified")
        @Expose
        var deleteAllowedSpecified = false

        @SerializedName("verificationAllowed")
        @Expose
        var verificationAllowed: Int = 0

        @SerializedName("verificationAllowedSpecified")
        @Expose
        var verificationAllowedSpecified = false

        @SerializedName("trialACHDate")
        @Expose
        var trialACHDate: String? = null

        @SerializedName("trialACHReturnDate")
        @Expose
        var trialACHReturnDate: String? = null

        @SerializedName("trialACHReturnCode")
        @Expose
        var trialACHReturnCode: String? = null

        @SerializedName("trialACHNOCCode")
        @Expose
        var trialACHNOCCode: String? = null

        @SerializedName("trialACHNOCData")
        @Expose
        var trialACHNOCData: String? = null

        @SerializedName("accountVerifiedOn")
        @Expose
        var accountVerifiedOn: String? = null
    }
}