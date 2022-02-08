package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.movocash.movo.data.model.responsemodel.sideMenuResponseModel.AvailableSubMenu




class sideMenuResponseModel {

    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class AvailableSubMenu {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("appId")
        @Expose
        var appId: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("isEnabled")
        @Expose
        var isEnabled: Boolean? = null

        @SerializedName("parentId")
        @Expose
        var parentId: Int? = null

        @SerializedName("availableSubMenu")
        @Expose
        var availableSubMenu: ArrayList<AvailableSubMenu>? = null
    }
    class Data {
        @SerializedName("availableSubMenu")
        @Expose
        var availableSubMenu: ArrayList<AvailableSubMenu>? = null
    }

}