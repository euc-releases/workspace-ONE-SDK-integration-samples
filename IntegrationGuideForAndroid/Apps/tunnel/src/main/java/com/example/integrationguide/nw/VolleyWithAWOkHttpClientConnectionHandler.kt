// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import com.airwatch.gateway.clients.AWOkHttpClient
import com.airwatch.util.Logger.d
import com.airwatch.util.Logger.i
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.integrationguide.OkHttpStack
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference

class VolleyWithAWOkHttpClientConnectionHandler(
    context: Context,
    listener: StatusListener? = null,
) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)
    private var context: WeakReference<Context> = WeakReference(context)


    companion object {
        private val TAG: String = VolleyWithAWOkHttpClientConnectionHandler::class.java.name
    }

    override fun connect(url: String) {
        loadUrlUsingVolleyWithAWOkHttpClient(url, listener.get())
    }

    /**
     * This method sets [OkHttpClient] using AWWrapped class - [AWOkHttpClient] as transport layer for Volley.
     * For this it uses abstraction layer, implementation of {@BaseHttpStack}.
     * Refer: [OkHttpStack]
     */
    private fun loadUrlUsingVolleyWithAWOkHttpClient(loadUrl: String, listener: StatusListener?) {
        val client = OkHttpClient()
        val stack = OkHttpStack(client)
        val requestQueue = Volley.newRequestQueue(context.get(), stack)
        requestQueue.start()
        val stringRequest: StringRequest = object : StringRequest(Method.GET, loadUrl,

            Response.Listener { response: String ->
                d(TAG, "Response is: $response")
            },

            Response.ErrorListener { error: VolleyError ->
                val status = "Error received: ${error.message}"
                d(TAG, status)
                listener?.onStatusUpdate(status)
            }) {
            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                val status = "AWOkHttpClient with Volley: ${response.statusCode}"
                listener?.onStatusUpdate(status)
                i(TAG, status)
                return super.parseNetworkResponse(response)
            }
        }
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest)
    }
}