// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.retrofit

import android.content.Context
import com.airwatch.util.Logger.w
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.airwatch.gateway.clients.AWOkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.X509TrustManager

/**
 * Creates Retrofit instance by using AWOkHttpClient.
 * tested: "https://reqres.in/"
 */
object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getClient(
        context: Context?,
        url: String,
        trustAllTrustManager: X509TrustManager?,
    ): Retrofit? {
        var urlPath = url
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        //Create OkHttpClient instance using AWOkHttpClient.
        // Here, it will use Android default trust store. To customize the SSL trust management,use
        //copyWithDefaults(okHttpClient,X509TrustManager) and implement X509TrustManager
        val awOkClient = AWOkHttpClient.copyWithDefaults(context, client, trustAllTrustManager!!)

        //Retrofit.Builder() expects a Client implementation.
        //Need to pass an OkHttpClient object as its parameter.
        if (urlPath.substring(urlPath.length - 1) != "/") {
            w("Retrofit", "Entered Url must end with /")
            urlPath = "$urlPath/"
        }
        retrofit = Retrofit.Builder()
            .baseUrl(urlPath)
            .client(awOkClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}