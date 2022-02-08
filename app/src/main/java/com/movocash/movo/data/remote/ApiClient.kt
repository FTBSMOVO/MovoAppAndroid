package com.movocash.movo.data.remote

import com.movocash.movo.MovoApp
import com.movocash.movo.utilities.Constants
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    private const val baseUrl = Constants.BASE_URL
    private var retrofit: Retrofit? = null
    private val dispatcher = Dispatcher()

    fun getClient(): Retrofit? {
        val okHttpClient: OkHttpClient =  OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.NONE
        if (retrofit == null) {
            retrofit = Retrofit.Builder().client(OkHttpClient().newBuilder().dispatcher(dispatcher).addInterceptor(Interceptor { chain: Interceptor.Chain? ->
                val newRequest = chain?.request()!!.newBuilder()
                newRequest.addHeader("authorization", "${MovoApp.db.getString(Constants.ACCESS_TOKEN)}")
                return@Interceptor chain.proceed(newRequest.build())
            }).addInterceptor(logging).build())
                    //.client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofit
    }


}