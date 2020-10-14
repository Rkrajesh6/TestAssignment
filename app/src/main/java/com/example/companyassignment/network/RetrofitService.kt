package com.example.companyassignment.network

import com.example.companyassignment.util.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private var mApiInterface: Api? = null

    fun create(): Api? {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.readTimeout(Utils.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        httpClient.connectTimeout(Utils.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        httpClient.addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()

        mApiInterface = retrofit.create(Api::class.java)
        return mApiInterface
    }

    fun searchApi() : Api?{
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Utils.SEARCH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.readTimeout(Utils.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        httpClient.connectTimeout(Utils.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        httpClient.addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()

        mApiInterface = retrofit.create(Api::class.java)
        return mApiInterface
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}