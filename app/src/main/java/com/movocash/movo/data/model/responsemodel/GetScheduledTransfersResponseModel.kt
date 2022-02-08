package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetScheduledTransfersResponseModel {
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

        @SerializedName("referenceID")
        @Expose
        var referenceID: String? = null

        @SerializedName("singleTransfers")
        @Expose
        var singleTransfers: ArrayList<SingleTransfer>? = null

        @SerializedName("recurringTransfers")
        @Expose
        var recurringTransfers: ArrayList<RecurringTransfer>? = null

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

    class SingleTransfer {
        @SerializedName("transferId")
        @Expose
        var transferId: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("transferDate")
        @Expose
        var transferDate: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("transferFrom")
        @Expose
        var transferFrom: String? = null

        @SerializedName("transferTo")
        @Expose
        var transferTo: String? = null

        @SerializedName("transferComments")
        @Expose
        var transferComments: String? = null

        @SerializedName("achType")
        @Expose
        var achType: Long = 0

        @SerializedName("achTypeSpecified")
        @Expose
        var achTypeSpecified = false

        @SerializedName("impactCard")
        @Expose
        var impactCard: Long = 0

        @SerializedName("originationDate")
        @Expose
        var originationDate: String? = null

        @SerializedName("debitTraceAuditNo")
        @Expose
        var debitTraceAuditNo: String? = null

        @SerializedName("creditTraceAuditNo")
        @Expose
        var creditTraceAuditNo: String? = null

        @SerializedName("returnCode")
        @Expose
        var returnCode: String? = null

        @SerializedName("returnDate")
        @Expose
        var returnDate: String? = null

        var frequencyType = 1
    }

    class RecurringTransfer {
        @SerializedName("recurrenceId")
        @Expose
        var recurrenceId: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("transferDate")
        @Expose
        var transferDate: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("transferFrom")
        @Expose
        var transferFrom: String? = null

        @SerializedName("transferTo")
        @Expose
        var transferTo: String? = null

        @SerializedName("transferComments")
        @Expose
        var transferComments: String? = null

        @SerializedName("transferFrequency")
        @Expose
        var transferFrequency: Long = 0

        @SerializedName("transferFrequencySpecified")
        @Expose
        var transferFrequencySpecified = false

        @SerializedName("transferContinuity")
        @Expose
        var transferContinuity: Long = 0

        @SerializedName("transferContinuitySpecified")
        @Expose
        var transferContinuitySpecified = false

        @SerializedName("transferCount")
        @Expose
        var transferCount: String? = null

        @SerializedName("transferEndDate")
        @Expose
        var transferEndDate: String? = null

        @SerializedName("transferAmount")
        @Expose
        var transferAmount: String? = null

        @SerializedName("amountPreference")
        @Expose
        var amountPreference: Long = 0

        @SerializedName("amountPreferenceSpecified")
        @Expose
        var amountPreferenceSpecified = false

        @SerializedName("daysBeforeDueDate")
        @Expose
        var daysBeforeDueDate: String? = null

        @SerializedName("thrshldLmtPrcntge")
        @Expose
        var thrshldLmtPrcntge: Long = 0

        @SerializedName("thrshldLmtPrcntgeSpecified")
        @Expose
        var thrshldLmtPrcntgeSpecified = false

        @SerializedName("autoPaymentOnSpecificDay")
        @Expose
        var autoPaymentOnSpecificDay: String? = null

        @SerializedName("achType")
        @Expose
        var achType: Long = 0

        @SerializedName("achTypeSpecified")
        @Expose
        var achTypeSpecified = false

        @SerializedName("transfers")
        @Expose
        var transfers: ArrayList<Transfer>? = null
    }

    class Transfer {
        @SerializedName("transferId")
        @Expose
        var transferId: String? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("transferDate")
        @Expose
        var transferDate: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("transferFrom")
        @Expose
        var transferFrom: String? = null

        @SerializedName("transferTo")
        @Expose
        var transferTo: String? = null

        @SerializedName("transferComments")
        @Expose
        var transferComments: String? = null

        @SerializedName("achType")
        @Expose
        var achType: Long = 0

        @SerializedName("achTypeSpecified")
        @Expose
        var achTypeSpecified = false

        @SerializedName("impactCard")
        @Expose
        var impactCard: Long = 0

        @SerializedName("originationDate")
        @Expose
        var originationDate: String? = null

        @SerializedName("debitTraceAuditNo")
        @Expose
        var debitTraceAuditNo: String? = null

        @SerializedName("creditTraceAuditNo")
        @Expose
        var creditTraceAuditNo: String? = null

        @SerializedName("returnCode")
        @Expose
        var returnCode: String? = null

        @SerializedName("returnDate")
        @Expose
        var returnDate: String? = null
    }
}