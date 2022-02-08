package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAuthTokenResponseModel {
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

        @SerializedName("arn")
        @Expose
        var arn: String? = null

        @SerializedName("signOnToken")
        @Expose
        var signOnToken: String? = null

    }
}