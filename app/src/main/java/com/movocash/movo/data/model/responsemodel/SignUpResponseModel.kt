package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignUpResponseModel {

    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var data: String? = null
   //var data: Model? = null

    /*class Model {
        @SerializedName("reviewStatus")
        @Expose
        var reviewStatus: String = ""
    }*/

}