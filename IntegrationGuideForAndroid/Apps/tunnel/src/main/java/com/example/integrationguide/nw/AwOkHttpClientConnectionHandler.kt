// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import com.airwatch.gateway.clients.AWOkHttpClient
import com.airwatch.util.Logger.e
import com.airwatch.util.Logger.i
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.ref.WeakReference

class AwOkHttpClientConnectionHandler(listener: StatusListener? = null) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)

    companion object {
        private val TAG: String = AwOkHttpClientConnectionHandler::class.java.name
    }

    override fun connect(url: String) {
        loadUrlInAwOkHttpClient(url, listener.get())
    }

    private fun loadUrlInAwOkHttpClient(loadUrl: String, listener: StatusListener?) {
        Thread {
            try {
                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url(loadUrl)
                    .build()
                val response = AWOkHttpClient.newCall(client, request).execute()
                val status = "AWOkHttpClient : ${response.code}"
                listener?.onStatusUpdate(status)
                i(TAG, status)
                response.body?.let {
                    i(TAG, status)
                    it.close()
                }
            } catch (e: Exception) {
                val status = "Unable to send request AWOkHttpClient ${e.message}"
                listener?.onStatusUpdate(status)
                e(TAG, status)
            }
        }.start()
    }
}