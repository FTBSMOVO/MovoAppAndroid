package com.plaid.linksample.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API calls to our localhost token server.
 */

interface LinkSampleApi {

  @GET("Banking/getlinktoken")
  fun getLinkToken(): Single<LinkToken>

  @GET("Banking/getlinktoken")
  fun getPlaidToken(): Call<UpdatedGeneralResponseModel>
}

data class LinkToken(
  @SerializedName("data") val link_token: String
)


class UpdatedGeneralResponseModel {
  @SerializedName("isError")
  @Expose
  var isError: Boolean = false
  @SerializedName("messages")
  @Expose
  var messages: String? = null
  @SerializedName("data")
  @Expose
  var data: String? = null
}


/*
interface LinkSampleApi {

  @POST("/api/create_link_token")
  fun getLinkToken(): Single<LinkToken>
}

data class LinkToken(
  @SerializedName("link_token") val link_token: String
)
*/

