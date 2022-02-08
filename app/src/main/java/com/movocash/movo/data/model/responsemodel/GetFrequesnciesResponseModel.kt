package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetFrequesnciesResponseModel {

    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var list: ArrayList<Model>? = null

    class Model {
        @SerializedName("id")
        @Expose
        var id: Int = 0

        @SerializedName("name")
        @Expose
        var name: String = ""

        @SerializedName("isRecurring")
        @Expose
        var isRecurring: Boolean = false
    }

}