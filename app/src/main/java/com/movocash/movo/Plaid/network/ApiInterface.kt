package com.plaid.linksample.network


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("Banking/getlinktoken")
    fun getPlaidToken(): Call<UpdatedGeneralResponseModel>

}