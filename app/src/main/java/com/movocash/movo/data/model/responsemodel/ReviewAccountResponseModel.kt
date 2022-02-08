package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewAccountResponseModel {


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
        @SerializedName("isEligibleForReview")
        @Expose
        var isEligibleForReview: Boolean = false

        @SerializedName("reviewCount")
        @Expose
        var reviewCount: Int = 0

        /*@SerializedName("code")
        @Expose
        var code: String = ""

        @SerializedName("description")
        @Expose
        var description: String = ""*/
    }
}