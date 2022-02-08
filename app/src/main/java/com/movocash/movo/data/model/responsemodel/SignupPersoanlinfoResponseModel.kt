package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignupPersoanlinfoResponseModel {

    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null
/*
    @SerializedName("data")
    @Expose
    var data: String? = null*/
    var data: Model? = null

    class Model {
        @SerializedName("userId")
        @Expose
        var userId: String = ""

        @SerializedName("decisionId")
        @Expose
        var decisionId: Int = 0

        @SerializedName("code")
        @Expose
        var code: String = ""

        @SerializedName("description")
        @Expose
        var description: String = ""
    }

}