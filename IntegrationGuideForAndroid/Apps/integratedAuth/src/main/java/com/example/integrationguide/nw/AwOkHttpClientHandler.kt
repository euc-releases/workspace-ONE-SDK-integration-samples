// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import com.airwatch.gateway.clients.AWOkHttpClient
import com.airwatch.util.Logger.e
import com.airwatch.util.Logger.i
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import okhttp3.*
import java.lang.Exception
import java.lang.ref.WeakReference
import java.security.cert.X509Certificate
import java.util.ArrayList
import java.util.HashMap
import javax.net.ssl.X509TrustManager

class AwOkHttpClientHandler(context: Context, listener: StatusListener? = null) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)
    private var context: WeakReference<Context> = WeakReference(context)

    companion object {
        private val TAG: String = AwOkHttpClientHandler::class.java.name
    }

    override fun connect(url: String) {
        invokeAWOkHttpClient(url)
    }

    /*
     * Default OkHttpClient and AWOkHttClient
     */
    private fun invokeAWOkHttpClient(url: String) {
        Thread {
            context.get()?.let {
                awOkHttpClientHandler(it, url)
            }
        }.start()
    }

    private fun awOkHttpClientHandler(context: Context, url: String) {
        try {
            val clientOrg: OkHttpClient = OkHttpClient.Builder()
                .cookieJar(okhttpCookieJar)
                .build()
            val client = AWOkHttpClient.copyWithDefaults(context,
                clientOrg,
                trustAllTrustManager)
            val request: Request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            val responseCode = response.code.toString()
            i(TAG,"Status code AwOkHttpClient :$responseCode")
            val message = response.body!!.string()
            i(TAG,"Default AWOkHttpClient response: $message")
            response.body!!.close()
            listener.get()?.onStatusUpdate(responseCode)
        } catch (e: Exception) {
            e(TAG, "${e.message}", "Unable to send request AWOkHttpClient", e)
            listener.get()?.onStatusUpdate("Error")
        }
    }

    private val okhttpCookieJar: CookieJar = object : CookieJar {
        private val cookieStore = HashMap<String, List<Cookie>>()
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host]
            return cookies ?: ArrayList()
        }
    }

    private val trustAllTrustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }

//    private fun loadUrlInAwOkHttpClient(loadUrl: String, listener: StatusListener?) {
//        Thread {
//            try {
//                val client = OkHttpClient()
//                val request: Request = Request.Builder()
//                    .url(loadUrl)
//                    .build()
//                val response = AWOkHttpClient.newCall(client, request).execute()
//                val status = "Status code AWOkHttpClient : ${response.code}"
//                listener?.onStatusUpdate(status)
//                i(TAG, status)
//                response.body?.let {
//                    i(TAG, status)
//                    it.close()
//                }
//            } catch (e: Exception) {
//                val status = "Unable to send request AWOkHttpClient ${e.message}"
//                listener?.onStatusUpdate(status)
//                e(TAG, status)
//            }
//        }.start()
//    }
}