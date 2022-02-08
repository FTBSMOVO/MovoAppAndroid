package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UploadMediaResponseModel {
    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    val messages: String? = null

    @SerializedName("data")
    @Expose
    val data: ArrayList<ImageData>? = null

    class ImageData {
        @SerializedName("originalFileName")
        @Expose
        val originalFileName: String = ""

        @SerializedName("url")
        @Expose
        val url: String = ""

        @SerializedName("thumbUrl")
        @Expose
        val thumbUrl: String = ""
    }
}