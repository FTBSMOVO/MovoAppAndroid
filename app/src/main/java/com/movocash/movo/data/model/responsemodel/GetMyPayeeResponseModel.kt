package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetMyPayeeResponseModel {
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

        @SerializedName("balance")
        @Expose
        var balance: Double = 0.0

        @SerializedName("balanceSpecified")
        @Expose
        var balanceSpecified = false

        @SerializedName("totalRecords")
        @Expose
        var totalRecords: String? = null

        @SerializedName("totalLength")
        @Expose
        var totalLength: String? = null

        @SerializedName("transId")
        @Expose
        var transId: String? = null

        @SerializedName("fee")
        @Expose
        var fee: String? = null

        @SerializedName("payees")
        @Expose
        var payees: ArrayList<Payee>? = null

        @SerializedName("audioRecords")
        @Expose
        var audioRecords: ArrayList<AudioRecord>? = null
    }

    class AudioRecord {
        @SerializedName("audioFile")
        @Expose
        var audioFile: String? = null

        @SerializedName("recordNumber")
        @Expose
        var recordNumber: String? = null

        @SerializedName("bankAccountNumber")
        @Expose
        var bankAccountNumber: String? = null

        @SerializedName("bpConsumerAccountNo")
        @Expose
        var bpConsumerAccountNo: String? = null

        @SerializedName("achAcctNick")
        @Expose
        var achAcctNick: String? = null

        @SerializedName("isTextNick")
        @Expose
        var isTextNick: String? = null

        @SerializedName("pwProgramId")
        @Expose
        var pwProgramId: String? = null
    }

    class Payee {
        @SerializedName("payeeSerialNo")
        @Expose
        var payeeSerialNo: String? = null

        @SerializedName("addressSerialNo")
        @Expose
        var addressSerialNo: String? = null

        @SerializedName("payeeId")
        @Expose
        var payeeId: String? = null

        @SerializedName("payeeName")
        @Expose
        var payeeName: String? = null

        @SerializedName("payeeAddress")
        @Expose
        var payeeAddress: String? = null

        @SerializedName("payeeAddress2")
        @Expose
        var payeeAddress2: String? = null

        @SerializedName("payeeAddress3")
        @Expose
        var payeeAddress3: String? = null

        @SerializedName("payeeAddress4")
        @Expose
        var payeeAddress4: String? = null

        @SerializedName("payeeCity")
        @Expose
        var payeeCity: String? = null

        @SerializedName("payeeState")
        @Expose
        var payeeState: String? = null

        @SerializedName("payeeCountryCode")
        @Expose
        var payeeCountryCode: String? = null

        @SerializedName("payeeCId")
        @Expose
        var payeeCId: String? = null

        @SerializedName("payeeZip")
        @Expose
        var payeeZip: String? = null

        @SerializedName("payeeNickname")
        @Expose
        var payeeNickname: String? = null

        @SerializedName("payeeAccountNumber")
        @Expose
        var payeeAccountNumber: String? = null

        @SerializedName("bankCode")
        @Expose
        var bankCode: String? = null

        @SerializedName("switchId")
        @Expose
        var switchId: String? = null
    }
}