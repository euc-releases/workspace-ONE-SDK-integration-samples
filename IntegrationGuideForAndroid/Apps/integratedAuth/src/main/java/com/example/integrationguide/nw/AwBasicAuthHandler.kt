// Copyright 2023 VMware, Inc.
// SPDX-License-Identifier: BSD-2-Clause

package com.example.integrationguide.nw

import android.content.Context
import com.airwatch.gateway.clients.AWUrlConnection
import com.airwatch.gateway.clients.BasicAuthHttpUrlConnection
import com.airwatch.util.Logger.d
import com.airwatch.util.Logger.e
import com.airwatch.util.Logger.i
import com.example.integrationguide.interfaces.ConnectionHandler
import com.example.integrationguide.interfaces.StatusListener
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class AwBasicAuthHandler(context: Context, listener: StatusListener? = null) : ConnectionHandler {

    private var listener: WeakReference<StatusListener> = WeakReference(listener)
    private var context: WeakReference<Context> = WeakReference(context)

    companion object {
        private val TAG: String = AwBasicAuthHandler::class.java.name
    }

    override fun connect(url: String) {
        initBasicAuth(url, listener.get())
    }

    private fun initBasicAuth(url: String, listener: StatusListener?) {
        Thread {
            d(TAG, "awHttpUrlConnectionHandler ")
            context.get()?.let {
                initBasicAuthConnection(it, url, listener)
            }
        }.start()
    }

    private fun initBasicAuthConnection(
        context: Context,
        loadUrl: String,
        listener: StatusListener?
    ) {
        var basicAuthHttpUrlConnection: BasicAuthHttpUrlConnection? = null
        try {
            val url = URL(loadUrl)
            val httpURLConnection = AWUrlConnection.openConnection(url) as HttpURLConnection
            basicAuthHttpUrlConnection =
                BasicAuthHttpUrlConnection(context, httpURLConnection, true)
            basicAuthHttpUrlConnection.useCaches = false
            basicAuthHttpUrlConnection.connectTimeout = 15000
            basicAuthHttpUrlConnection.connect()
            listener?.onStatusUpdate("${basicAuthHttpUrlConnection.responseCode}")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            if (basicAuthHttpUrlConnection!!.responseCode >= 400) {
                logResponse(basicAuthHttpUrlConnection.errorStream)
            } else {
                logResponse(basicAuthHttpUrlConnection.inputStream)
            }
        } catch (e: Exception) {
            e(TAG, "Exception ${e.message}", e)
        } finally {
            basicAuthHttpUrlConnection?.disconnect()
        }
    }

    @Throws(IOException::class)
    private fun logResponse(inputStream: InputStream) {
        val sb = StringBuilder()
        val buf = ByteArray(128)
        while (inputStream.read(buf) != -1) {
            sb.append(String(buf))
        }
        i(TAG, "AwHttpUrlConnection response: $sb")
    }

}