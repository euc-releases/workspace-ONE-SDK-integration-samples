// Copyright 2023 Omnissa, LLC.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import com.airwatch.gateway.clients.AWOkHttpClient
import com.airwatch.util.Logger.d
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import com.example.integrationguide.retrofit.RetrofitAPIInterface
import com.example.integrationguide.retrofit.RetrofitClient
import com.example.integrationguide.retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class RetrofitWithAWOKHttpClientConnectionHandler(
    context: Context,
    listener: StatusListener? = null,
) : ConnectionHandler {

    private var context: WeakReference<Context> = WeakReference(context)
    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    companion object {
        private val TAG: String = RetrofitWithAWOKHttpClientConnectionHandler::class.java.simpleName
    }

    override fun connect(url: String) {
        getResponseUsingRetrofitWithAWOkHttpClient(url, listener.get())
    }

    /**
     * This method refers to Retrofit usage using [AWOkHttpClient]. Refer [RetrofitClient]
     */
    private fun getResponseUsingRetrofitWithAWOkHttpClient(
        loadUrl: String,
        listener: StatusListener?,
    ) {
        val apiInterface =
            RetrofitClient.getClient(context.get(), loadUrl, trustAllTrustManager)?.create(
                RetrofitAPIInterface::class.java)

        /**
         * GET List Resources
         */
        val user = apiInterface?.userInfo
        user?.enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                val status =
                    "Retrofit Response is: ${response.body()?.userInfo?.firstName}\n ${response.body()?.userInfo?.emailInfo} ${response.code()}"
                listener?.onStatusUpdate(status)
                d(TAG, status)
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                val status = "Retrofit onFailure"
                listener?.onStatusUpdate(status)
                d(TAG, "$status: ${t.message}")
            }
        })
    }

    private val trustAllTrustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }
}