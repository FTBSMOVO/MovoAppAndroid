package com.movocash.movo.data.model.responsemodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponseModel {

    @SerializedName("isError")
    @Expose
    var isError: Boolean = false

    @SerializedName("messages")
    @Expose
    var messages: String? = null

    @SerializedName("data")
    @Expose
    var data: Model? = null

    class Model {
        @SerializedName("token")
        @Expose
        var token: String = ""

        @SerializedName("profilePicture")
        @Expose
        var profilePicture: String = ""

        @SerializedName("profilePictureThumb")
        @Expose
        var profilePictureThumb: String = ""

        @SerializedName("email")
        @Expose
        var email: String = ""

        @SerializedName("username")
        @Expose
        var username: String = ""

        @SerializedName("firstName")
        @Expose
        var firstName: String = ""

        @SerializedName("middleName")
        @Expose
        var middleName: String = ""

        @SerializedName("lastName")
        @Expose
        var lastName: String = ""

        @SerializedName("userId")
        @Expose
        var userId: String = ""

        @SerializedName("lastLogin")
        @Expose
        var lastLogin: String? = null

        @SerializedName("accountInfo")
        @Expose
        var accountInfo: AccountInfo? = null
    }

    class AccountInfo {
        @SerializedName("genderId")
        @Expose
        var genderId: Int = 0

        @SerializedName("identificationTypeId")
        @Expose
        var identificationTypeId: Int = 0

        @SerializedName("identificationValue")
        @Expose
        var identificationValue: String = ""

        @SerializedName("email")
        @Expose
        var email: String = ""

        @SerializedName("cellCountryCode")
        @Expose
        var cellCountryCode: String = ""

        @SerializedName("cellPhoneNumber")
        @Expose
        var cellPhoneNumber: String = ""

        @SerializedName("addressLine1")
        @Expose
        var addressLine1: String = ""

        @SerializedName("country")
        @Expose
        var country: String = ""

        @SerializedName("state")
        @Expose
        var state: String = ""

        @SerializedName("city")
        @Expose
        var city: String = ""

        @SerializedName("zipCode")
        @Expose
        var zipCode: String = ""

        @SerializedName("dateOfBirth")
        @Expose
        var dateOfBirth: String? = null
    }

    class shippingAddress{

        @SerializedName("isPhysicalCardOrdered")
        @Expose
        var isPhysicalCardOrdered: String = ""

        @SerializedName("addressLine1")
        @Expose
        var addressLine1: String = ""

        @SerializedName("country")
        @Expose
        var country: String = ""

        @SerializedName("state")
        @Expose
        var state: String = ""

        @SerializedName("stateIso2")
        @Expose
        var stateIso2: String = ""

        @SerializedName("city")
        @Expose
        var city: String = ""

        @SerializedName("zipCode")
        @Expose
        var zipCode: String = ""

    }
}