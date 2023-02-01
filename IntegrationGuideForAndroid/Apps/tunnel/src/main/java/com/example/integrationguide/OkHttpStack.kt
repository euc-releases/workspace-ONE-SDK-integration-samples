// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide

import android.util.Log
import okhttp3.OkHttpClient
import com.android.volley.toolbox.BaseHttpStack
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.airwatch.gateway.clients.AWOkHttpClient
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.toolbox.HttpResponse
import okhttp3.Headers
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class OkHttpStack(private val mClient: OkHttpClient) : BaseHttpStack() {

    companion object {
        private val TAG = OkHttpStack::class.java.name
    }

    @Throws(IOException::class, AuthFailureError::class)
    override fun executeRequest(
        request: Request<*>,
        additionalHeaders: Map<String, String>,
    ): HttpResponse {

        //Set OkHttpClient new builder
        val clientBuilder: OkHttpClient.Builder = mClient.newBuilder()
        val timeoutMs = request.timeoutMs

        //Set Volley request timer and set up the url
        clientBuilder.connectTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        clientBuilder.readTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        clientBuilder.writeTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        val okHttpRequestBuilder = okhttp3.Request.Builder()
        okHttpRequestBuilder.url(request.url)

        //Include headers in OkHttpRequest
        val headers = request.headers
        for (name in headers.keys) {
            okHttpRequestBuilder.addHeader(name, headers[name]!!)
        }
        for (name in additionalHeaders.keys) {
            okHttpRequestBuilder.addHeader(name, additionalHeaders[name]!!)
        }

        //Execute the okHttpRequest and get the response
        val client: OkHttpClient = clientBuilder.build()
        val okHttpRequest: okhttp3.Request = okHttpRequestBuilder.build()
        val okHttpResponse = AWOkHttpClient.newCall(client, okHttpRequest).execute()
        Log.d(TAG, "okHttpResponse $okHttpResponse")

        //Convert the response to com.android.volley.toolbox.HttpResponse
        val code = okHttpResponse.code
        val body = okHttpResponse.body
        val content = body?.byteStream()
        val contentLength = body?.contentLength()?.toInt() ?: 0
        val responseHeaders = mapHeaders(okHttpResponse.headers)
        return HttpResponse(code, responseHeaders, contentLength, content)
    }

    private fun mapHeaders(responseHeaders: Headers): List<Header> {
        val headers: MutableList<Header> = ArrayList()
        var i = 0
        val len = responseHeaders.size
        while (i < len) {
            val name = responseHeaders.name(i)
            val value = responseHeaders.value(i)
            headers.add(Header(name, value))
            i++
        }
        return headers
    }
}